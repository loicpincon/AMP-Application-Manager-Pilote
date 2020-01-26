package application.manager.pilote.docker.deployer;

import java.util.HashMap;
import java.util.Map;

import application.manager.pilote.application.modele.AngularApplication;
import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

public class AngularAppDeployer extends DefaultDeployer<AngularApplication> {

	@Builder
	public AngularAppDeployer(Application app, Instance ins, Server server, Environnement env, ContainerParam param) {
		super((AngularApplication) app, ins, server, env, param);
	}

	@Override
	public void run() {
		logger.debug("Debut du deploiement de angular application");
		if (!app.getIsBuilder()) {
			logger.debug("Telechargement et Build de l'application --prod en cours");
			executionScript(scriptPathHelper.getPathOfFile("", "deploiement_angular_build"), genererCheminTemporaire(),
					app.getBaseLocation(), param.getVersion());
			logger.debug("Fin Telechargement et Build de l'application --prod ");
		}
		lancementDeploiement();
	}

	/**
	 * 
	 */
	private void lancementDeploiement() {
		Map<String, String> params = new HashMap<>();
		params.put("dossier", "dist/" + app.getBaseName());
		createContainer(params);
	}

}
