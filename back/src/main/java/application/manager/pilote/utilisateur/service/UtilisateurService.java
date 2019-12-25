package application.manager.pilote.utilisateur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository uRepo;

	@Autowired
	private HashService hashService;

	public Utilisateur inserer(Utilisateur u) {
		u.setToken(hashService.hash(u.getLogin() + u.getPassword()));
		return uRepo.insert(u);
	}

	public Utilisateur modifier(String id, Utilisateur utilisateur) {
		return uRepo.save(utilisateur);
	}

	public Utilisateur consulter(String id) {
		return uRepo.consulter(id).get();
	}

}
