package application.manager.pilote.utilisateur.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.service.DefaultService;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.utilisateur.helper.DroitApplicatifHelper;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;
import application.manager.pilote.utilisateur.modele.DroitApplicatifLevel;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.repository.UtilisateurRepository;

@Service
public class UtilisateurService extends DefaultService {

	@Autowired
	private UtilisateurRepository uRepo;

	@Autowired
	private HashService hashService;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private DroitApplicatifHelper droitApplicatifHelper;

	public Utilisateur inserer(Utilisateur u) {
		u.setToken(hashService.hash(u.getLogin() + u.getPassword()));
		return uRepo.insert(u);
	}

	public Utilisateur modifier(Utilisateur utilisateur) {
		return uRepo.save(utilisateur);
	}

	public Utilisateur consulter(String token) {
		Optional<Utilisateur> uOpt = uRepo.findByToken(token);
		if (!uOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "Utilisateur non trouve");
		}
		return uOpt.get();
	}

	public List<Utilisateur> recuperer(String idApp) {
		if (idApp == null) {
			return uRepo.findAll();
		} else {
			List<Utilisateur> list = uRepo.findUserByApplication(idApp);
			for (Utilisateur user : list) {
				List<DroitApplicatif> listToRemove = new ArrayList<>();
				for (DroitApplicatif droit : user.getRights()) {
					if (!droit.getApplicationId().equals(idApp)) {
						listToRemove.add(droit);
					}
				}
				user.getRights().removeAll(listToRemove);
			}
			return list;
		}
	}

	public Utilisateur ajouterDroit(String id, String idApplication, String level) {
		Utilisateur us = consulter(id);
		ajouterDroitUser(us, DroitApplicatif.builder().applicationId(idApplication).date(new Date())
				.level(DroitApplicatifLevel.PROP).build(), level);
		return uRepo.save(us);
	}

	public DroitApplicatif ajouterDroitApplicatifs(String id, DroitApplicatif droit, String accessLevel) {
		Utilisateur us = consulter(id);
		DroitApplicatif droitU = ajouterDroitUser(us, droit, accessLevel);
		uRepo.save(us);
		return droitU;
	}

	private DroitApplicatif ajouterDroitUser(Utilisateur us, DroitApplicatif da, String level) {
		Application app = appService.consulter(da.getApplicationId());
		if (us.getRights() == null) {
			us.setRights(new ArrayList<>());
		}
		for (DroitApplicatif droitU : us.getRights()) {
			if (droitU.getApplicationId().equals(da.getApplicationId())) {
				LOG.debug("l'utilisateur possede deja les droits");
				return droitApplicatifHelper.setDroitApplicatif(droitU, level);
			}
		}

		LOG.debug("l'utilisateur a maintenant les droits de " + level + " sur l'application " + app.getName());
		us.getRights().add(droitApplicatifHelper.setDroitApplicatif(da, level));
		return da;
	}

}
