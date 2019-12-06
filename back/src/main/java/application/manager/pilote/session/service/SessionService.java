package application.manager.pilote.session.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.session.modele.UserSession;
import application.manager.pilote.session.modele.Utilisateur;
import application.manager.pilote.session.repository.UtilisateurRepository;

@Service
public class SessionService {

	@Autowired
	private UtilisateurRepository userRepo;

	/**
	 * 
	 * @param param
	 * @return
	 */
	public UserSession connexion(UserSessionParam param) {
		Optional<Utilisateur> u = userRepo.trouverParLoginEtMotDePasse(param.getLogin(), param.getPassword());
		if (u.isEmpty()) {
			throw new ApplicationException(BAD_REQUEST, "Identifiant ou mot de passe incorrect");
		}
		return UserSession.builder().token(u.get().getToken()).build();
	}

}
