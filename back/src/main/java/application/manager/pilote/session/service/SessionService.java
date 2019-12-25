package application.manager.pilote.session.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.session.modele.UserSession;
import application.manager.pilote.session.repository.UserSessionRepository;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.repository.UtilisateurRepository;

@Service
public class SessionService {

	@Autowired
	private UtilisateurRepository userRepo;

	@Autowired
	private UserSessionRepository userSessionRepo;

	/**
	 * 
	 * @param param
	 * @return
	 */
	public UserSession connexion(UserSessionParam param) {
		Optional<Utilisateur> u = userRepo.trouverParLoginEtMotDePasse(param.getLogin(), param.getPassword());
		if (!u.isPresent()) {
			throw new ApplicationException(BAD_REQUEST, "Identifiant ou mot de passe incorrect");
		}
		UserSession session = UserSession.builder().token(u.get().getToken()).build();
		userSessionRepo.insert(session);
		return session;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public void deconnexion(String token) {
		userSessionRepo.deleteById(token);
	}

}
