package organisation.application.manager.pilote.docker.deployer;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import organisation.application.manager.pilote.application.modele.AngularApplication;
import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.Environnement;
import organisation.application.manager.pilote.application.modele.Instance;
import organisation.application.manager.pilote.docker.service.pr.ContainerParam;
import organisation.application.manager.pilote.server.modele.Server;

public class AngularAppDeployer extends DefaultDeployer<AngularApplication> {

	@Builder
	public AngularAppDeployer(Application app, Instance ins, Server server, Environnement env, ContainerParam param) {
		super((AngularApplication) app, ins, server, env, param);
	}

	@Override
	public void run() {
		logger.debug("Debut du deploiement de angular organisation.application");
		if (!app.getIsBuilder()) {
			logger.debug("Telechargement et Build de l'organisation.application --prod en cours");
			executionScript(scriptPathHelper.getPathOfFile("", "deploiement_angular_build"), genererCheminTemporaire(),
					app.getBaseLocation(), param.getVersion());
			logger.debug("Fin Telechargement et Build de l'organisation.application --prod ");
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
