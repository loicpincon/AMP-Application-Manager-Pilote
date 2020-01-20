package application.manager.pilote.docker.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.modele.WarApplication;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.docker.helper.DeployFileHelper;
import application.manager.pilote.docker.helper.ScriptPathHelper;
import application.manager.pilote.docker.service.DockerContainerService;
import application.manager.pilote.docker.service.DockerFileService;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

@Builder
public class DockerWarDeployer extends DefaultDeployer {

	private static final String BASE_PATH_TO_APPLICATION_STOCK = "BASE_PATH_TO_APPLICATION_STOCK";

	private static final String SLASH = "/";

	protected static final Log LOG = LogFactory.getLog(DockerWarDeployer.class);

	private static final String BASE_PATH_TO_DOCKERFILE_DEFAULT = "/var/base/dockerfile";

	private static final String BASE_PATH_TO_DOCKERFILE = "BASE_PATH_TO_DOCKERFILE";

	@Autowired
	private DockerClient dockerClient;

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
	private ApplicationService appService;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ScriptPathHelper scriptPathHelper;

	@Autowired
	DockerContainerService dockerContainerService;

	Environnement envChoisi;

	ContainerParam param;

	WarApplication app;

	Server server;

	Instance ins;

	/**
	 * @param serveur
	 * @return
	 */
	private Ports getPortsBinds(Instance ins) {
		Ports portBindings = new Ports();
		ExposedPort expoPort = new ExposedPort(8080);
		portBindings.bind(expoPort, Binding.bindPort(Integer.valueOf(ins.getPort())));
		return portBindings;
	}

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
				this.dockerContainerService.manage(app.getId(), server.getId(), ins.getId(), "delete");
			}

			String pathToFolderTemporaire = stringUtils.concat(
					properties.getPropertyOrElse(BASE_PATH_TO_DOCKERFILE, BASE_PATH_TO_DOCKERFILE_DEFAULT), SLASH,
					hasherService.randomInt());

			BuildImageResultCallback callback = new BuildImageResultCallback();
			File dockerFile = deployfileHelper.createDockerFile(pathToFolderTemporaire,
					dockerFileService.get(app.getDockerFileId()));

			String pathToWar = properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + SLASH + app.getId() + SLASH
					+ param.getVersion() + SLASH + app.getBaseName();

			File copied = new File(pathToFolderTemporaire + SLASH + app.getBaseName());
			FileUtils.copyFile(new File(pathToWar), copied);
			deployfileHelper.createGcpFile(pathToFolderTemporaire, parametres.getParametres());

			ProcessBuilder pb = new ProcessBuilder(scriptPathHelper.getPathOfFile("", "deploiement_war_gcp"),
					pathToFolderTemporaire, pathToFolderTemporaire + SLASH + app.getBaseName(),
					pathToFolderTemporaire + SLASH + "ROOT.war" + " .");

			pb.start();
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = br.readLine()) != null) {
				LOG.debug(line);
			}

			LOG.debug("Fin de la construction, debut de la creation de l'image");

			String test = dockerClient.buildImageCmd(dockerFile).withBuildArg("basename", app.getBaseName())
					.exec(callback).awaitCompletion().awaitImageId();
			LOG.info(test);
			CreateContainerResponse container = dockerClient.createContainerCmd(test)
					.withName(param.getIdInstanceCible()).withPublishAllPorts(true).withName(ins.getId())
					.withPortBindings(getPortsBinds(ins)).exec();

			dockerClient.startContainerCmd(container.getId()).exec();
			ins.setEtat("L");
			if (server.getDns() != null) {
				ins.setUrl("http://" + server.getDns() + ":" + ins.getPort());
			} else {
				ins.setUrl("http://" + server.getIp() + ":" + ins.getPort());
			}
			ins.setVersionApplicationActuel(param.getVersion());
			ins.setVersionParametresActuel("0.0.0");
			ins.getUserActions().add(traceAction("Deploy", "Succes", param.getVersion()));
			template.convertAndSend("/content/application", ins);
			LOG.debug("fin du thread");
			appService.modifier(app);

		} catch (DockerException | InterruptedException | IOException | NullPointerException e) {
			LOG.error(e);
			ins.setEtat("S");
			ins.getUserActions().add(traceAction("Deploy", "Echec", param.getVersion()));
			appService.modifier(app);
			template.convertAndSend("/content/application", ins);
			Thread.currentThread().interrupt();
			throw new ApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
