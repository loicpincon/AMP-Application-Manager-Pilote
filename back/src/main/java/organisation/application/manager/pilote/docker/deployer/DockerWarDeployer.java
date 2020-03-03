package organisation.application.manager.pilote.docker.deployer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import lombok.Builder;
import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.Environnement;
import organisation.application.manager.pilote.application.modele.Instance;
import organisation.application.manager.pilote.application.modele.ParametreSeries;
import organisation.application.manager.pilote.application.modele.WarApplication;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.docker.service.pr.ContainerParam;
import organisation.application.manager.pilote.server.modele.Server;

public class DockerWarDeployer extends DefaultDeployer<WarApplication> {

	@Builder
	public DockerWarDeployer(Application app, Instance ins, Server server, Environnement env, ContainerParam param) {
		super((WarApplication) app, ins, server, env, param);
	}

	public void run() {
		creerFichierParametresApplicatifs();
		deplacerFichierWarDansRepertoireCourant();
		executionScript(scriptPathHelper.getPathOfFile("", "deploiement_war_gcp"), genererCheminTemporaire(),
				genererCheminTemporaire() + SLASH + app.getBaseName(), genererCheminTemporaire() + SLASH + "ROOT.war",
				app.getNomFichierProperties());
		lancementDeploiement();
	}

	private void lancementDeploiement() {
		Map<String, String> params = new HashMap<>();
		params.put("basename", "ROOT.war");
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

	private void enableHttpsInContainer() {
		try {
			String server = URLDecoder.decode(getClass().getResource("/data/tomcat/web-https.xml").getFile(), "UTF-8");
			String web = URLDecoder.decode(getClass().getResource("/data/tomcat/server-https.xml").getFile(), "UTF-8");
			File copiedServer = new File(genererCheminTemporaire() + SLASH + "server.xml");
			File copiedWeb = new File(genererCheminTemporaire() + SLASH + "web.xml");

			FileUtils.copyFile(new File(server), copiedServer);
			FileUtils.copyFile(new File(web), copiedWeb);
		} catch (IOException e) {
			logger.error(e);
			throw new ApplicationException(400, "Les fichiers de configuration HTTPS sont non trouvés");
		}
	}

}
