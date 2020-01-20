package application.manager.pilote.utilisateur.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.mail.MailService;
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

	@Autowired
	private MailService mailService;

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

	/**
	 * @param idApp
	 * @param keyword
	 * @return
	 */
	public List<Utilisateur> recuperer(String idApp, String keyword) {
		if (idApp != null) {
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
		} else if (keyword != null) {
			return uRepo.findByNomOrPrenomOrLogin(keyword);
		} else {
			return uRepo.findAll();
		}
	}

	public Utilisateur ajouterDroit(String id, String idApplication, String level) {
		Utilisateur us = consulter(id);
		ajouterDroitUser(us, DroitApplicatif.builder().applicationId(idApplication).date(new Date())
				.level(DroitApplicatifLevel.PROP).build(), level);
		return uRepo.save(us);
	}

	public DroitApplicatif ajouterDroitApplicatifs(String id, DroitApplicatif droit, String accessLevel,
			Boolean delete) {
		if (delete) {
			Utilisateur us = consulter(id);
			supprimerDroitUser(us, droit);
			return null;
		} else {
			if (accessLevel != null && DroitApplicatifLevel.isPresent(accessLevel)) {
				Utilisateur us = consulter(id);
				DroitApplicatif droitU = ajouterDroitUser(us, droit, accessLevel);
				uRepo.save(us);
				return droitU;
			} else {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, "Ce droit n'existe pas");
			}
		}

	}

	private DroitApplicatif ajouterDroitUser(Utilisateur us, DroitApplicatif da, String level) {
		Application app = appService.consulter(da.getApplicationId());
		if (us.getRights() == null) {
			us.setRights(new ArrayList<DroitApplicatif>());
		}
		for (DroitApplicatif droitU : us.getRights()) {
			if (droitU.getApplicationId().equals(da.getApplicationId())) {
				LOG.debug("l'utilisateur possede deja les droits");
				mailService.sendMail(us.getLogin(), "Droit sur l'application : " + app.getName(),
						"Vos droits ont évolués, vous avez maintenant les droits de "
								+ DroitApplicatifLevel.valueOf(level).getLibelle() + " sur l'application "
								+ app.getName());
				return droitApplicatifHelper.setDroitApplicatif(droitU, level);
			}
		}

		mailService.sendMail(us.getLogin(), "Droit sur l'application : " + app.getName(),
				"vous avez maintenant les droits de " + DroitApplicatifLevel.valueOf(level).getLibelle()
						+ " sur l'application " + app.getName());
		LOG.debug("l'utilisateur a maintenant les droits de " + level + " sur l'application " + app.getName());
		us.getRights().add(droitApplicatifHelper.setDroitApplicatif(da, level));
		return da;
	}

	private void supprimerDroitUser(Utilisateur us, DroitApplicatif da) {
		if (us.getRights() == null) {
			us.setRights(new ArrayList<DroitApplicatif>());
		}
		Boolean find = false;
		Iterator<DroitApplicatif> i = us.getRights().iterator();
		while (i.hasNext()) {
			DroitApplicatif s = i.next();
			if (s.getApplicationId().equals(da.getApplicationId())) {
				i.remove();
				find = true;
			}
		}

		for (DroitApplicatif droitU : us.getRights()) {
			if (droitU.getApplicationId().equals(da.getApplicationId())) {
				us.getRights().remove(droitU);
				find = true;
			}
		}
		if (!find) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, "Ce droit n'existe pas pour cet user");

		}
		uRepo.save(us);
	}

}
