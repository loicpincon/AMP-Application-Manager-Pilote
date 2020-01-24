package application.manager.pilote.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.AngularApplication;
import application.manager.pilote.application.modele.Livrable;
import application.manager.pilote.application.service.rs.GitHubReleaseResult;
import application.manager.pilote.commun.service.DefaultService;

@Service
public class LivrableService extends DefaultService {

	private static String GITHUB_URL = "https://api.github.com/repos/$1$/$2$/tags";

	public List<Livrable> getLivrableFromGitHub(AngularApplication angularApp) {
		List<Livrable> livrables = new ArrayList<>();
		ResponseEntity<GitHubReleaseResult[]> retour = http.getForEntity(GITHUB_URL
				.replace("$1$", angularApp.getUserProprietaire()).replace("$2$", angularApp.getNomRepository()),
				GitHubReleaseResult[].class);
		for (GitHubReleaseResult release : retour.getBody()) {
			livrables.add(map(release));
		}
		return livrables;
	}

	private Livrable map(GitHubReleaseResult param) {
		Livrable livrable = new Livrable();
		livrable.setNom(param.getName());
		return livrable;
	}

}
