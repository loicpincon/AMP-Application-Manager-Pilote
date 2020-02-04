package application.manager.pilote.docker.deployer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.modele.WarApplication;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

public class DockerWarDeployer extends DefaultDeployer<WarApplication> {

	@Builder
	public DockerWarDeployer(Application app, Instance ins, Server server, Environnement env, ContainerParam param) {
		super((WarApplication) app, ins, server, env, param);
	}

	public void run() {
		creerFichierParametresApplicatifs();
		deplacerFichierWarDansRepertoireCourant();
		executionScript(scriptPathHelper.getPathOfFile("", "deploiement_war_gcp"), genererCheminTemporaire(),
				genererCheminTemporaire() + SLASH + app.getBaseName(),
				genererCheminTemporaire() + SLASH + "ROOT.war" + " .", app.getNomFichierProperties());
		lancementDeploiement();
	}

	private void lancementDeploiement() {
		Map<String, String> params = new HashMap<>();
		params.put("basename", app.getBaseName());
		createContainer(params);
	}

	private void creerFichierParametresApplicatifs() {
		ParametreSeries parametres = getParameterSeries();
		deployfileHelper.createGcpFile(genererCheminTemporaire() + "/" + app.getNomFichierProperties(),
				parametres.getParametres());
	}

	private ParametreSeries getParameterSeries() {
		for (ParametreSeries paramB : envChoisi.getParametres()) {
			if (paramB.getVersion().equals(param.getVersionParam())) {
				return paramB;
			}
		}
		throw new ApplicationException(400, "La version des parametres est introuvable");
	}

	private void deplacerFichierWarDansRepertoireCourant() {
		String pathOfWar = properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + SLASH + app.getId() + SLASH
				+ param.getVersion() + SLASH + app.getBaseName();
		try {
			File copied = new File(genererCheminTemporaire() + SLASH + app.getBaseName());
			FileUtils.copyFile(new File(pathOfWar), copied);
		} catch (IOException e) {
			logger.error(e);
			throw new ApplicationException(400, "Le livrable est introuvable");
		}
	}

}
