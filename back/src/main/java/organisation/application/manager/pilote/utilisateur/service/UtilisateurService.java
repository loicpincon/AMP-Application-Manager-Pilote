package organisation.application.manager.pilote.utilisateur.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.service.ApplicationService;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.mail.MailService;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.commun.service.HashService;
import organisation.application.manager.pilote.utilisateur.helper.DroitApplicatifHelper;
import organisation.application.manager.pilote.utilisateur.modele.DroitApplicatif;
import organisation.application.manager.pilote.utilisateur.modele.DroitApplicatifLevel;
import organisation.application.manager.pilote.utilisateur.modele.Utilisateur;
import organisation.application.manager.pilote.utilisateur.repository.UtilisateurRepository;

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
		List<Utilisateur> options = uRepo.trouverParEmail(u.getEmail());
		if (!options.isEmpty() && search(options, u.getLogin())) {
			throw new ApplicationException(400, "Le mail est déjà utilisé");
		}
		try {
			u.setImage(new Binary(IOUtils.toByteArray(getClass().getResource("/img/avatar.png"))));
		} catch (IOException e) {
			LOG.error(e);
		}
		return uRepo.insert(u);
	}

	public Utilisateur modifier(Utilisateur utilisateur) {
		return uRepo.save(utilisateur);
	}

	public Utilisateur modifier(String id, Utilisateur utilisateur) {
		Utilisateur us = consulter(id);
		List<Utilisateur> options = uRepo.trouverParEmail(utilisateur.getEmail());
		if (!options.isEmpty() && !search(options, id)) {
			throw new ApplicationException(400, "Le mail est déjà utilisé");
		}
		us.setEmail(utilisateur.getEmail());
		us.setNom(utilisateur.getNom());
		us.setPrenom(utilisateur.getPrenom());
		return uRepo.save(us);
	}

	private Boolean search(List<Utilisateur> users, String id) {
		for (Utilisateur u : users) {
			if (!u.getLogin().equals(id)) {
				return false;
			}
		}
		return true;
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
				mailService.sendMail(us.getLogin(), "Droit sur l'organisation.application : " + app.getName(),
						"Vos droits ont évolués, vous avez maintenant les droits de "
								+ DroitApplicatifLevel.valueOf(level).getLibelle() + " sur l'organisation.application "
								+ app.getName());
				return droitApplicatifHelper.setDroitApplicatif(droitU, level);
			}
		}

		mailService.sendMail(us.getLogin(), "Droit sur l'organisation.application : " + app.getName(),
				"vous avez maintenant les droits de " + DroitApplicatifLevel.valueOf(level).getLibelle()
						+ " sur l'organisation.application " + app.getName());
		LOG.debug("l'utilisateur a maintenant les droits de " + level + " sur l'organisation.application " + app.getName());
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

	public Utilisateur setPhotoToMembre(String token, MultipartFile file) throws IOException {
		Utilisateur m = consulter(token);
		m.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		return uRepo.save(m);
	}

}
