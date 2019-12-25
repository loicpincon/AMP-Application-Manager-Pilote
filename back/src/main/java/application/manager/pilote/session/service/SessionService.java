package application.manager.pilote.session.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.session.modele.UserSession;
import application.manager.pilote.session.repository.UserSessionRepository;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.repository.UtilisateurRepository;

@Service
public class SessionService {

	private static final String X_TOKEN_UTILISATEUR = "X-TOKEN-UTILISATEUR";

	@Autowired
	private UtilisateurRepository userRepo;

	@Autowired
	private UserSessionRepository userSessionRepo;

	@Autowired
	private HttpServletRequest request;

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
		Utilisateur user = u.get();
		UserSession session = UserSession.builder().token(user.getToken()).nom(user.getNom()).prenom(user.getPrenom())
				.rights(user.getRights()).build();
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

	public UserSession getSession() {
		String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR);
		Optional<UserSession> u = userSessionRepo.findById(tokenUserHeader);
		if (!u.isPresent()) {
			throw new ApplicationException(HttpStatus.UNAUTHORIZED,
					"Une session est nescessaire pour acceder à cette ressource");
		}
		return u.get();
	}

}
