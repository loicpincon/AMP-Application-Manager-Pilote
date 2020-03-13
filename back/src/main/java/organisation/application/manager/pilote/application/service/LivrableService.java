package organisation.application.manager.pilote.application.service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import organisation.application.manager.pilote.application.modele.AngularApplication;
import organisation.application.manager.pilote.application.modele.Livrable;
import organisation.application.manager.pilote.application.service.rs.GitHubReleaseResult;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.helper.PropertiesReader;
import organisation.application.manager.pilote.commun.service.DefaultService;

@Service
public class LivrableService extends DefaultService {

	private static final String BASE_PATH_TO_APPLICATION_STOCK = "BASE_PATH_TO_APPLICATION_STOCK";

	private static String GITHUB_URL = "https://api.github.com/repos/$1$/$2$/tags";

	@Autowired
	private ApplicationService appService;

	@Autowired
	private PropertiesReader properties;

	public List<Livrable> getLivrableFromGitHub(AngularApplication angularApp) {
		List<Livrable> livrables = new ArrayList<>();
		if (angularApp.getNomRepository() != null && angularApp.getUserProprietaire() != null) {
			LOG.debug("Recuperation des livrable");
			try {
				ResponseEntity<GitHubReleaseResult[]> retour = http.getForEntity(GITHUB_URL
						.replace("$1$", angularApp.getUserProprietaire()).replace("$2$", angularApp.getNomRepository()),
						GitHubReleaseResult[].class);
				for (GitHubReleaseResult release : retour.getBody()) {
					livrables.add(map(release));
				}
			} catch (RestClientException e) {
				LOG.error(e.getMessage());
			}

		}
		return livrables;
	}

	private Livrable map(GitHubReleaseResult param) {
		Livrable livrable = new Livrable();
		livrable.setNom(param.getName());
		livrable.setDateUpload(new Date());
		return livrable;
	}

	public Livrable consulter(List<Livrable> livrables, String id) {
		for (Livrable livrable : livrables) {
			if (livrable.getId().equals(id)) {
				return livrable;
			}
		}
		throw new ApplicationException(HttpStatus.BAD_REQUEST, "Identifiant livrable inconnu");
	}

	public Resource telechargerVersion(String idApp, String idVersion) {
		return loadFileAsResource(properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + "/"
				+ consulter(appService.consulter(idApp).getLivrables(), idVersion).getPathtoFile());
	}

	public Resource loadFileAsResource(String pathToFile) {

		try {
			Path filePath = FileSystems.getDefault().getPath(new File(pathToFile).getPath());
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, "File not found ");
			}
		} catch (MalformedURLException ex) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

}
