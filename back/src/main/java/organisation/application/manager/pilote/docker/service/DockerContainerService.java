package organisation.application.manager.pilote.docker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;

import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.ApplicationType;
import organisation.application.manager.pilote.application.modele.Environnement;
import organisation.application.manager.pilote.application.modele.Instance;
import organisation.application.manager.pilote.application.modele.UserAction;
import organisation.application.manager.pilote.application.service.ApplicationService;
import organisation.application.manager.pilote.application.service.InstanceService;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.docker.deployer.AngularAppDeployer;
import organisation.application.manager.pilote.docker.deployer.DefaultDeployer;
import organisation.application.manager.pilote.docker.deployer.DockerJarDeployer;
import organisation.application.manager.pilote.docker.deployer.DockerWarDeployer;
import organisation.application.manager.pilote.docker.mapper.ContainerMapper;
import organisation.application.manager.pilote.docker.modele.ActionInstance;
import organisation.application.manager.pilote.docker.modele.Container;
import organisation.application.manager.pilote.docker.modele.EtatInstance;
import organisation.application.manager.pilote.docker.service.pr.ContainerParam;
import organisation.application.manager.pilote.server.modele.Server;
import organisation.application.manager.pilote.server.service.ServerService;
import organisation.application.manager.pilote.session.modele.UserSession;
import organisation.application.manager.pilote.session.service.SessionService;
import organisation.application.manager.pilote.socket.service.InstanceWSService;

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
	private InstanceWSService instanceWS;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * @param dockerFile
	 * @throws IOException
	 */
	public Instance createContainer(ContainerParam param) {
		UserSession userSesion = sessionService.getSession();
		Application app = appService.consulter(param.getIdApplicationCible());
		Server server = serveurService.consulter(param.getIdServeurCible());
		Environnement envChoisi = app.getEnvironnements().get(server.getId());
		Instance ins = instanceService.consulter(envChoisi.getInstances(), param.getIdInstanceCible());

		DefaultDeployer<? extends Application> deployer = null;
		if (app.getType().equals(ApplicationType.WAR)) {
			deployer = DockerWarDeployer.builder().app(app).env(envChoisi).param(param).ins(ins).server(server).build();
		} else if (app.getType().equals(ApplicationType.ANGULAR)) {
			deployer = AngularAppDeployer.builder().app(app).param(param).env(envChoisi).server(server).ins(ins)
					.build();
		} else if (app.getType().equals(ApplicationType.JAR)) {
			deployer = DockerJarDeployer.builder().app(app).env(envChoisi).param(param).ins(ins).server(server).build();
		} else {
			throw new ApplicationException(400,
					"Impossible de deployer ce type d'organisation.application : " + app.getType());
		}
		deployer.setUser(userSesion);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(deployer);
		deployer.start();

		LOG.debug("retourne au client");
		ins.setVersionApplicationActuel("Deploiement en cours de la version " + param.getVersion());
		ins.setVersionParametresActuel(param.getVersionParam());
		ins.setEtat(EtatInstance.P.name());
		appService.modifier(app);
		instanceWS.sendDetailsInstance(ins);
		return ins;
	}

	/**
	 * @return
	 */
	public List<Container> getInstances() {
		List<Container> retour = new ArrayList<>();
		for (com.github.dockerjava.api.model.Container container : dockerClient.listContainersCmd().exec()) {
			retour.add(this.containerMapper.mapFrom(container));
		}
		return retour;
	}

	/**
	 * @return
	 */
	public List<com.github.dockerjava.api.model.Container> getContainers() {
		return dockerClient.listContainersCmd().exec();

	}

	/**
	 * @param containerId
	 */
	private void startC(Instance containerId) {
		try {
			dockerClient.startContainerCmd(containerId.getContainerId()).exec();
			containerId.setEtat(EtatInstance.L.name());
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
				LOG.debug("action demandée : " + action);
				if (action.equals(ActionInstance.DEPLOY.getCode())) {
					startC(instance);
				} else if (action.equals(ActionInstance.RELOAD.getCode())) {
					reload(instance);
				} else if (action.equals(ActionInstance.STOP.getCode())) {
					stopC(instance);
				} else if (action.equals(ActionInstance.DELETE.getCode())) {
					delete(instance);
				} else {
					throw new ApplicationException(400, "action inconnue --> " + action);
				}
				instance.getUserActions()
						.add(traceAction(action, "Success", instance.getVersionApplicationActuel(), userSesion));
				appService.modifier(appli);
				instanceWS.sendDetailsInstance(instance);
			}
		}.start();
		instance.setVersionApplicationActuel(
				action + " en cours de la version " + instance.getVersionApplicationActuel());
		instance.setEtat(EtatInstance.P.name());
		appService.modifier(appli);
		instanceWS.sendDetailsInstance(instance);
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
			containerId.setEtat(EtatInstance.S.name());
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
			containerId.setEtat(EtatInstance.L.name());
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
		instance.setEtat(EtatInstance.V.name());
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
