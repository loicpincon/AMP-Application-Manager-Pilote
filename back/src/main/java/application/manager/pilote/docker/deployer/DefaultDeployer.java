package application.manager.pilote.docker.deployer;

import java.io.File;
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
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.application.modele.Application;
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
import application.manager.pilote.session.modele.UserSession;

public abstract class DefaultDeployer<E extends Application> extends Thread {

	protected static final Log LOG = LogFactory.getLog(DefaultDeployer.class);

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

	private String pathFolderTemporaire;

	protected UserSession user;

	protected E app;

	protected Instance ins;

	protected DefaultDeployer(E app, Instance ins) {
		this.app = app;
		this.ins = ins;
	}

	@Override
	public abstract void run();

	protected abstract Ports getPortsBinds(Instance ins);

	protected UserAction traceAction(String libelle, String status, String version) {
		UserAction us = new UserAction();
		us.setDate(new Date());
		us.setLibelle(libelle);
		us.setMembre(user.getNom() + " " + user.getPrenom());
		us.setStatus(status);
		us.setVersion(version);
		return us;
	}

	public void setUser(UserSession s) {
		this.user = s;
	}

	protected String genererCheminTemporaire() {
//		if (pathFolderTemporaire == null) {
//			pathFolderTemporaire = stringUtils.concat(
//					properties.getPropertyOrElse(BASE_PATH_TO_DOCKERFILE, BASE_PATH_TO_DOCKERFILE_DEFAULT), SLASH,
//					hasherService.randomInt());
//			LOG.debug("Chemin temporaire : " + pathFolderTemporaire);
//			new File(pathFolderTemporaire).mkdirs();
//		}
//		return pathFolderTemporaire;
		return "C:\\Users\\LoïcPinçon\\Desktop\\DockerFile\\148578267";
	}

	/**
	 * 
	 * @return
	 */
	protected BuildImageResultCallback getCallBackBuildImage() {
		return new BuildImageResultCallback();
	}

	protected File createDockerFile() {
		return deployfileHelper.createDockerFile(genererCheminTemporaire(), app.getDockerfile());

	}

	protected BuildImageCmd buildImageDocker() {
		LOG.debug("Fin de la construction, debut de la creation de l'image");
		return dockerClient.buildImageCmd(createDockerFile());
	}

	protected BuildImageCmd buildImageDocker(Map<String, String> buildArgs) {
		LOG.debug("Fin de la construction, debut de la creation de l'image avec " + buildArgs.size() + " parametres");
		BuildImageCmd command = dockerClient.buildImageCmd(createDockerFile());
		Set<String> cles = buildArgs.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()) {
			String cle = it.next();
			LOG.debug(cle + " : " + buildArgs.get(cle));
			command.withBuildArg(cle, buildArgs.get(cle));
		}
		return command;
	}

	protected void createContainer(Map<String, String> params) {
		try {
			LOG.debug("Construction de l'image");
			String imageId = buildImageDocker(params).exec(getCallBackBuildImage()).awaitCompletion().awaitImageId();
			LOG.debug("Fin de la construction, debut de la creation du container");
			dockerClient.createContainerCmd(imageId).withPublishAllPorts(true).withName(ins.getId())
					.withPortBindings(getPortsBinds(ins)).exec();
			LOG.debug("Fin de la  creation du container");
			LOG.debug("Lancement du container avec identifiant : " + ins.getContainerId());

			dockerClient.startContainerCmd(ins.getContainerId()).exec();
		} catch (InterruptedException e) {
			LOG.error(e);
			Thread.currentThread().interrupt();
			throw new ApplicationException(500, "Impossible de constuire le container : " + e.getMessage());
		}
	}

}
