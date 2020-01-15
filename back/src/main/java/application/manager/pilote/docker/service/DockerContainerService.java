package application.manager.pilote.docker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.ApplicationType;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.UserAction;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.application.service.InstanceService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.docker.deployer.DockerWarDeployer;
import application.manager.pilote.docker.mapper.ContainerMapper;
import application.manager.pilote.docker.modele.Container;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;
import application.manager.pilote.session.modele.UserSession;
import application.manager.pilote.session.service.SessionService;

@Service
public class DockerContainerService {

	protected static final Log LOG = LogFactory.getLog(DockerContainerService.class);

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ContainerMapper containerMapper;

	@Autowired
	private InstanceService instanceService;

	@Autowired
	private ServerService serveurService;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private SessionService sessionService;

	/**
	 * @param dockerFile
	 * @throws IOException
	 */
	public Instance createContainer(ContainerParam param) throws IOException {
		UserSession userSesion = sessionService.getSession();
		Application app = appService.consulter(param.getIdApplicationCible());
		Server server = serveurService.consulter(param.getIdServeurCible());
		Environnement envChoisi = app.getEnvironnements().get(server.getId());
		Instance ins = instanceService.consulter(envChoisi.getInstances(), param.getIdInstanceCible());

		if (app.getType().equals(ApplicationType.WAR)) {
			DockerWarDeployer deployer = DockerWarDeployer.builder().app(app).envChoisi(envChoisi).param(param).ins(ins)
					.server(server).build();
			deployer.setUser(userSesion);
			deployer.start();
		}

		LOG.debug("retourne au client");
		ins.setEtat("P");
		appService.modifier(app);
		template.convertAndSend("/content/application", ins);
		return ins;
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
		UserSession userSesion = sessionService.getSession();
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
				instance.getUserActions()
						.add(traceAction(action, "Success", instance.getVersionApplicationActuel(), userSesion));
				appService.modifier(appli);
				template.convertAndSend("/content/application", instance);

			}
		}.start();

		instance.setEtat("P");
		appService.modifier(appli);
		template.convertAndSend("/content/application", instance);
		return instance;
	}

	protected UserAction traceAction(String libelle, String status, String version, UserSession userSesion) {
		UserAction us = new UserAction();
		us.setDate(new Date());
		us.setLibelle(libelle);
		us.setMembre(userSesion.getNom() + " " + userSesion.getPrenom());
		us.setStatus(status);
		us.setVersion(version);
		return us;
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
