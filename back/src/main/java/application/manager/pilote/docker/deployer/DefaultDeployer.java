package application.manager.pilote.docker.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.UserAction;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.docker.helper.DeployFileHelper;
import application.manager.pilote.docker.helper.ScriptPathHelper;
import application.manager.pilote.docker.service.DockerContainerService;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.session.modele.UserSession;

public abstract class DefaultDeployer<E extends Application> extends Thread {

	protected static final String BASE_PATH_TO_APPLICATION_STOCK = "BASE_PATH_TO_APPLICATION_STOCK";

	protected static final String SLASH = "/";

	protected static final String BASE_PATH_TO_DOCKERFILE_DEFAULT = "/var/base/dockerfile";

	protected static final String BASE_PATH_TO_DOCKERFILE = "BASE_PATH_TO_DOCKERFILE";

	@Autowired
	protected DeployFileHelper deployfileHelper;

	@Autowired
	protected PropertiesReader properties;

	@Autowired
	protected HashService hasherService;

	@Autowired
	protected DockerClient dockerClient;

	@Autowired
	protected StringHelper stringUtils;

	@Autowired
	protected ApplicationService appService;

	@Autowired
	protected SimpMessagingTemplate template;

	@Autowired
	protected ScriptPathHelper scriptPathHelper;

	@Autowired
	protected DockerContainerService dockerContainerService;

	protected Log logger;

	private String pathFolderTemporaire;

	protected UserSession user;

	protected E app;

	protected Instance ins;

	protected Server server;

	protected Environnement envChoisi;

	protected ContainerParam param;

	protected DefaultDeployer(E app, Instance ins, Server server, Environnement env, ContainerParam param) {
		this.logger = LogFactory.getLog(getClass());
		this.app = app;
		this.ins = ins;
		this.server = server;
		this.envChoisi = env;
		this.param = param;
	}

	/**
	 * 
	 */
	@Override
	public abstract void run();

	/**
	 * 
	 * @param libelle
	 * @param status
	 * @param version
	 * @return
	 */
	protected UserAction traceAction(String libelle, String status, String version) {
		UserAction us = new UserAction();
		us.setDate(new Date());
		us.setLibelle(libelle);
		us.setMembre(user.getNom() + " " + user.getPrenom());
		us.setStatus(status);
		us.setVersion(version);
		return us;
	}

	/**
	 * 
	 * @param s
	 */
	public void setUser(UserSession s) {
		this.user = s;
	}

	/**
	 * 
	 * @return
	 */
	protected String genererCheminTemporaire() {
		if (pathFolderTemporaire == null) {
			pathFolderTemporaire = stringUtils.concat(
					properties.getPropertyOrElse(BASE_PATH_TO_DOCKERFILE, BASE_PATH_TO_DOCKERFILE_DEFAULT), SLASH,
					hasherService.randomInt());
			logger.debug("Chemin temporaire : " + pathFolderTemporaire);
			new File(pathFolderTemporaire).mkdirs();
		}
		// return "C:\\Users\\LoïcPinçon\\Desktop\\DockerFile\\1510827207\\";
		return pathFolderTemporaire;
	}

	/**
	 * 
	 * @return
	 */
	protected BuildImageResultCallback getCallBackBuildImage() {
		return new BuildImageResultCallback();
	}

	/**
	 * 
	 * @return
	 */
	protected File createDockerFile() {
		return deployfileHelper.createDockerFile(genererCheminTemporaire(), app.getDockerfile());

	}

	/**
	 * 
	 * @return
	 */
	protected BuildImageCmd buildImageDocker() {
		return dockerClient.buildImageCmd(createDockerFile());
	}

	/**
	 * 
	 * @param buildArgs
	 * @return
	 */
	protected BuildImageCmd buildImageDocker(Map<String, String> buildArgs) {
		BuildImageCmd command = dockerClient.buildImageCmd(createDockerFile());
		Set<String> cles = buildArgs.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()) {
			String cle = it.next();
			logger.debug(cle + " : " + buildArgs.get(cle));
			command.withBuildArg(cle, buildArgs.get(cle));
		}
		return command;
	}

	/**
	 * 
	 * @param params
	 */
	protected void createContainer(Map<String, String> params) {
		try {
			if (ins.getEtat().equals("L") || ins.getEtat().equals("S")) {
				this.dockerContainerService.manage(app.getId(), server.getId(), ins.getId(), "delete");
				supprimerImageId(app.getDockerfile().getImageId());
			}
			updateInfosInstance("P", "Deploy", "Success", null);

			logger.debug("Construction de l'image");

			String imageId = buildImageDocker(params).exec(getCallBackBuildImage()).awaitCompletion().awaitImageId();
			app.getDockerfile().setImageId(imageId);
			logger.debug("Fin de la construction, debut de la creation du container");
			if (ins.getContainerId() != null) {
				dockerClient.removeContainerCmd(ins.getContainerId());
			}
			dockerClient.createContainerCmd(app.getDockerfile().getImageId()).withPublishAllPorts(true)
					.withName(ins.getId()).withPortBindings(getPortsBinds()).exec();
			logger.debug("Fin de la  creation du container");
			logger.debug("Lancement du container avec identifiant : " + ins.getContainerId());
			dockerClient.startContainerCmd(ins.getContainerId()).exec();
			updateInfosInstance("L", "Deploy", "Success", null);
		} catch (DockerException | InterruptedException e) {
			logger.error(e);
			updateInfosInstance("S", "Deploy", "Echec", "Probleme durant l'installation");
			Thread.currentThread().interrupt();
			throw new ApplicationException(500, "Impossible de constuire le container : " + e.getMessage());
		}
		supprimerEnvironnementTemporaire(genererCheminTemporaire());
	}

	/**
	 * 
	 * @param etat
	 * @param appVersion
	 * @param paramVersion
	 * @param action
	 * @param etatLibelle
	 * @param message
	 */
	protected void updateInfosInstance(String etat, String action, String etatLibelle, String message) {
		ins.setEtat(etat);
		if (server.getDns() != null) {
			ins.setUrl("http://" + server.getDns() + ":" + ins.getPort());
		} else {
			ins.setUrl("http://" + server.getIp() + ":" + ins.getPort());
		}
		ins.setLibelleEtatAction(message);
		ins.setLibelleVersion(param.getVersion());
		ins.setVersionApplicationActuel(param.getVersion());
		ins.setVersionParametresActuel(param.getVersionParam());
		ins.getUserActions().add(traceAction(action, etatLibelle, param.getVersion()));
		sendInfoToInterface();
		logger.debug("fin du thread avec le statut : " + etatLibelle);
		appService.modifier(app);
	}

	private void sendInfoToInterface() {
		template.convertAndSend("/content/application", ins);
	}

	/**
	 * @param serveur
	 * @return
	 */
	protected Ports getPortsBinds() {
		Ports portBindings = new Ports();
		ExposedPort expoPort = new ExposedPort(app.getDockerfile().getExposedPortInside());
		portBindings.bind(expoPort, Binding.bindPort(Integer.valueOf(ins.getPort())));
		return portBindings;
	}

	protected void executionScript(String... command) {
		logger.debug("Debut du script de creation");
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			logger.debug(command);
			pb.start();
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				logger.debug(line);
			}
			logger.debug("Fin du script de creation");
		} catch (IOException e) {
			logger.error(e);
			throw new ApplicationException(400, "Probleme durant le script de deploiement");
		}
	}

	private void supprimerEnvironnementTemporaire(String path) {
		// TODO supprimerEnvironnementTemporaire
	}

	private void supprimerImageId(String imageid) {
		// dockerClient.removeImageCmd(imageid).exec();
	}

}
