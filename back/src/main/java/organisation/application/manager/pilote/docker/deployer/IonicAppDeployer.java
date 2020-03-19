package organisation.application.manager.pilote.docker.deployer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import lombok.Builder;
import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.Environnement;
import organisation.application.manager.pilote.application.modele.Instance;
import organisation.application.manager.pilote.application.modele.IonicApplication;
import organisation.application.manager.pilote.application.modele.ParametreSeries;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.docker.service.pr.ContainerParam;
import organisation.application.manager.pilote.server.modele.Server;

public class IonicAppDeployer extends DefaultDeployer<IonicApplication> {

	@Builder
	public IonicAppDeployer(Application app, Instance ins, Server server, Environnement env, ContainerParam param) {
		super((IonicApplication) app, ins, server, env, param);
	}

	@Override
	public void run() {
		try {
			CloneCommand cloneCommand = Git.cloneRepository();
			cloneCommand.setURI(app.getRepositoryUrl());
			cloneCommand.setCredentialsProvider(
					new UsernamePasswordCredentialsProvider(app.getRepoUser(), app.getRepoPass()));
			cloneCommand.setDirectory(new File(genererCheminTemporaire()));
			cloneCommand.call();
			creerFichierParametresApplicatifs();
			lancementDeploiement();
		} catch (GitAPIException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	private void creerFichierParametresApplicatifs() {
		ParametreSeries parametres = getParameterSeries();
		deployfileHelper.createAngularFile(genererCheminTemporaire() + "/src/environments/environment.ts",
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

	/**
	 * 
	 */
	private void lancementDeploiement() {
		Map<String, String> params = new HashMap<>();
		params.put("folder", genererCheminTemporaire());
		createContainer(params);
	}

}
