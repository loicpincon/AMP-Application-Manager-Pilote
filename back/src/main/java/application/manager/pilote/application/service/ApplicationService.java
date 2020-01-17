package application.manager.pilote.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.repository.ApplicationRepository;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.service.DefaultService;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.service.UtilisateurService;

@Service
public class ApplicationService extends DefaultService {

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private HashService hashService;

	@Autowired
	private UtilisateurService userService;

	public Application consulter(String id) {
		Optional<Application> appOpt = appRepo.findById(id);
		if (!appOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "application non trouve");
		}
		return appOpt.get();
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
		Application app = consulter(idApp);
		Environnement env = app.getEnvironnements().get(idEnv);
		param.setDerniereModification(new Date());
		env.getParametres().add(param);
		modifier(app);
		return param;
	}

}
