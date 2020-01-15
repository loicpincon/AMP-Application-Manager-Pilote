package application.manager.pilote.docker.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.application.service.InstanceService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.commun.service.ShellService;
import application.manager.pilote.docker.helper.DeployFileHelper;
import application.manager.pilote.docker.mapper.ContainerMapper;
import application.manager.pilote.docker.modele.Container;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class DockerContainerService {

	private static final String BASE_PATH_TO_APPLICATION_STOCK = "BASE_PATH_TO_APPLICATION_STOCK";

	private static final String SLASH = "/";

	protected static final Log LOG = LogFactory.getLog(DockerContainerService.class);

	private static final String BASE_PATH_TO_DOCKERFILE_DEFAULT = "/var/base/dockerfile";

	private static final String BASE_PATH_TO_DOCKERFILE = "BASE_PATH_TO_DOCKERFILE";

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ContainerMapper containerMapper;

	@Autowired
	private DockerFileService dockerFileService;

	@Autowired
	private DeployFileHelper deployfileHelper;

	@Autowired
	private PropertiesReader properties;

	@Autowired
	private HashService hasherService;

	@Autowired
	private StringHelper stringUtils;

	@Autowired
	private InstanceService instanceService;

	@Autowired
	private ServerService serveurService;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ShellService shellService;

	/**
	 * @param dockerFile
	 * @throws IOException
	 */
	public Instance createContainer(ContainerParam param) throws IOException {

		String containerName = stringUtils.concat(param.getIdApplicationCible(), "-", param.getIdInstanceCible());
		Application app = appService.consulter(param.getIdApplicationCible());
		Server server = serveurService.consulter(param.getIdServeurCible());
		Environnement envChoisi = app.getEnvironnements().get(server.getId());
		Instance ins = instanceService.consulter(envChoisi.getInstances(), param.getIdInstanceCible());

		new Thread() {
			public void run() {
				try {
					ParametreSeries parametres = null;

					for (ParametreSeries paramB : envChoisi.getParametres()) {
						if (paramB.getVersion().equals(param.getVersionParam())) {
							parametres = paramB;
							break;
						}
					}

					if (ins.getEtat().equals("L") || ins.getEtat().equals("S")) {
						manage(app.getId(), server.getId(), ins.getId(), "delete");
					}
					String pathToFolderTemporaire = stringUtils.concat(
							properties.getPropertyOrElse(BASE_PATH_TO_DOCKERFILE, BASE_PATH_TO_DOCKERFILE_DEFAULT),
							SLASH, hasherService.randomInt());

					BuildImageResultCallback callback = new BuildImageResultCallback();
					File dockerFile = deployfileHelper.createDockerFile(pathToFolderTemporaire,
							dockerFileService.get(app.getDockerFileId()));

					String pathToWar = properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + SLASH + app.getId()
							+ SLASH + param.getVersion() + SLASH + app.getBaseName();

					File copied = new File(pathToFolderTemporaire + SLASH + app.getBaseName());
					FileUtils.copyFile(new File(pathToWar), copied);

					shellService.execute("cd " + pathToFolderTemporaire + " && exec jar -xvf " + pathToFolderTemporaire
							+ SLASH + app.getBaseName());
					shellService.execute("rm -rf " + pathToFolderTemporaire + SLASH + app.getBaseName());

					deployfileHelper.createGcpFile(pathToFolderTemporaire, parametres.getParametres());

					shellService.execute("cd " + pathToFolderTemporaire + " && exec jar -cf " + pathToFolderTemporaire
							+ SLASH + "ROOT.war" + " .");

					// CONSTRUCTION DU FICHIER DE PROPERTIES

					String test = dockerClient.buildImageCmd(dockerFile).exec(callback).awaitCompletion()
							.awaitImageId();
					LOG.info(test);
					CreateContainerResponse container = dockerClient.createContainerCmd(test)
							.withName(param.getIdInstanceCible()).withPublishAllPorts(true).withName(containerName)
							.withPortBindings(getPortsBinds(ins, server)).exec();

					dockerClient.startContainerCmd(container.getId()).exec();

					ins.setContainerId(container.getId());

					ins.setLibelle(param.getVersion());
					ins.setEtat("L");
					if (server.getDns() != null) {
						ins.setUrl("http://" + server.getDns() + ":" + ins.getPort());
					} else {
						ins.setUrl("http://" + server.getIp() + ":" + ins.getPort());
					}
					ins.setVersionApplicationActuel(param.getVersion());
					ins.setVersionParametresActuel("0.0.0");
					appService.modifier(app);
					template.convertAndSend("/content/application", ins);
					LOG.debug("fin du thread");
				} catch (DockerException | InterruptedException | IOException e) {
					ins.setEtat("S");
					appService.modifier(app);
					template.convertAndSend("/content/application", ins);
					Thread.currentThread().interrupt();
					throw new ApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
				}
			}
		}.start();

		LOG.debug("retourne au client");
		ins.setEtat("P");
		appService.modifier(app);
		template.convertAndSend("/content/application", ins);
		return ins;
	}

	/**
	 * @param serveur
	 * @return
	 */
	private Ports getPortsBinds(Instance ins, Server serveur) {
		Ports portBindings = new Ports();
		ExposedPort expoPort = new ExposedPort(8080);
		portBindings.bind(expoPort, Binding.bindPort(Integer.valueOf(ins.getPort())));
		return portBindings;
	}

	/**
	 * @return
	 */
	public List<Container> getContainers() {
		List<Container> retour = new ArrayList<>();
		for (com.github.dockerjava.api.model.Container container : dockerClient.listContainersCmd().exec()) {
			retour.add(this.containerMapper.mapFrom(container));
		}
		return retour;
	}

	/**
	 * @param containerId
	 */
	private void startC(Instance containerId) {
		try {
			dockerClient.startContainerCmd(containerId.getContainerId()).exec();
			containerId.setEtat("L");
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	/**
	 * @param id
	 * @param action
	 * @return
	 */
	public Instance manage(String app, Integer server, String id, String action) {

		Application appli = appService.consulter(app);
		Instance instance = instanceService.consulter(appli.getEnvironnements().get(server).getInstances(), id);
		new Thread() {
			@Override
			public void run() {
				switch (action) {
				case "start":
					startC(instance);
					break;
				case "reload":
					reload(instance);
					break;
				case "stop":
					stopC(instance);
					break;
				case "delete":
					delete(instance);
					break;
				default:
					throw new ApplicationException(400, "action inconnue");
				}
				appService.modifier(appli);
				template.convertAndSend("/content/application", instance);

			}
		}.start();

		instance.setEtat("P");
		appService.modifier(appli);
		template.convertAndSend("/content/application", instance);
		return instance;
	}

	/**
	 * @param containerId
	 */
	private void stopC(Instance containerId) {
		try {
			dockerClient.stopContainerCmd(containerId.getContainerId()).exec();
			containerId.setEtat("S");
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	/**
	 * @param containerId
	 */
	private void reload(Instance containerId) {
		try {
			dockerClient.restartContainerCmd(containerId.getContainerId()).exec();
			containerId.setEtat("L");
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	/**
	 * @param instance
	 */
	public void delete(Instance instance) {
		String status = dockerClient.inspectContainerCmd(instance.getContainerId()).exec().getState().getStatus();
		if (status != null && status.equals("running")) {
			this.stopC(instance);
		}
		instance.setVersionApplicationActuel(null);
		instance.setVersionParametresActuel(null);
		instance.setEtat("V");
		dockerClient.removeContainerCmd(instance.getContainerId()).exec();
	}

	/**
	 * @param containerId
	 * @return
	 */
	public InspectContainerResponse inspect(String containerId) {
		try {
			return dockerClient.inspectContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

}
