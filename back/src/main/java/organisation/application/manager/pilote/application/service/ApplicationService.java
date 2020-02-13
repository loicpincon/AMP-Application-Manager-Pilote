package organisation.application.manager.pilote.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.application.modele.AngularApplication;
import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.ApplicationType;
import organisation.application.manager.pilote.application.modele.Environnement;
import organisation.application.manager.pilote.application.modele.ParametreSeries;
import organisation.application.manager.pilote.application.repository.ApplicationRepository;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.commun.service.HashService;
import organisation.application.manager.pilote.utilisateur.modele.DroitApplicatif;
import organisation.application.manager.pilote.utilisateur.modele.Utilisateur;
import organisation.application.manager.pilote.utilisateur.service.UtilisateurService;

@Service
public class ApplicationService extends DefaultService {

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private HashService hashService;

	@Autowired
	private UtilisateurService userService;

	@Autowired
	private LivrableService livrableService;

	public Application consulter(String id) {
		Optional<Application> appOpt = appRepo.findById(id);
		if (!appOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "organisation.application non trouve");
		}
		Application app = appOpt.get();
		if (app.getType().equals(ApplicationType.ANGULAR)) {
			app.setLivrables(livrableService.getLivrableFromGitHub((AngularApplication) app));
		}

		Collections.sort(app.getLivrables(), Collections.reverseOrder());

		return app;
	}

	public List<Application> recuperer() {
		return appRepo.findAll();
	}

	public Application inserer(String idUser, Application param) {
		param.setId(hashService.hash(new Date() + param.getName()));
		param.getEnvironnements().put(1, new Environnement());
		Application appInsert = appRepo.insert(param);
		userService.ajouterDroit(idUser, appInsert.getId(), "PROP");
		return appInsert;
	}

	public Application modifier(String id, Application param) {
		Application app = consulter(id);
		app = param;
		app.setId(id);
		return appRepo.save(app);
	}

	public Application modifier(Application param) {
		return appRepo.save(param);
	}

	public List<Application> recupererParUser(String idUser) {
		Utilisateur us = userService.consulter(idUser);
		List<String> idApplicationAutorises = new ArrayList<>();
		if (us.getRights() != null) {
			for (DroitApplicatif da : us.getRights()) {
				idApplicationAutorises.add(da.getApplicationId());
			}
			return appRepo.findByIdIn(idApplicationAutorises);
		} else {
			return new ArrayList<>();
		}

	}

	public ParametreSeries ajouterSerieParametre(String idApp, Integer idEnv, ParametreSeries param) {
		if (param.getVersion() == null || param.getVersion().equals("")) {
			throw new ApplicationException(400, "Le nom de la version ne peut etre nul");
		}
		Application app = consulter(idApp);
		Environnement env = app.getEnvironnements().get(idEnv);
		param.setDerniereModification(new Date());
		env.getParametres().add(param);
		modifier(app);
		return param;
	}

	public ParametreSeries modifierParametreSerie(String id, Integer serveur, String version,
			ParametreSeries parametre) {
		Application app = consulter(id);
		Environnement env = app.getEnvironnements().get(serveur);
		for (ParametreSeries param : env.getParametres()) {
			if (param.getVersion().equals(version)) {
				param.setDerniereModification(new Date());
				param.setParametres(parametre.getParametres());
				modifier(app);
				return param;

			}
		}
		throw new ApplicationException(400, "Probleme pendant l'ajout des parametres");
	}

	public ParametreSeries consulterSerieParametre(String id, Integer serveur, String version) {
		Application app = consulter(id);
		Environnement env = app.getEnvironnements().get(serveur);
		for (ParametreSeries param : env.getParametres()) {
			if (param.getVersion().equals(version)) {
				return param;
			}
		}
		throw new ApplicationException(400, "Probleme pendant la consultation des parametres");
	}

}
