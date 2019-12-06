package application.manager.pilote.session.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

import application.manager.pilote.session.modele.Utilisateur;
import application.manager.pilote.session.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository uRepo;

	public Utilisateur inserer(Utilisateur u) {
		u.setToken(Hashing.sha256().hashString(u.getLogin() + u.getPassword(), UTF_8).toString());
		return uRepo.insert(u);
	}

	public Utilisateur modifier(String id, Utilisateur utilisateur) {
		return uRepo.save(utilisateur);
	}

}
