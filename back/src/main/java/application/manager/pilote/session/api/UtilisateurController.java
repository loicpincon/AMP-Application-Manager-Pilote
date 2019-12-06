package application.manager.pilote.session.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.session.modele.Utilisateur;
import application.manager.pilote.session.service.UtilisateurService;

@RestController
@RequestMapping("/users")
@ApiManager("Utilisateur")
public class UtilisateurController {

	@Autowired
	private UtilisateurService userService;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = POST)
	public Callable<ResponseEntity<Utilisateur>> ajouter(@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.inserer(utilisateur));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Callable<ResponseEntity<Utilisateur>> modifier(@PathVariable String id,
			@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.modifier(id,utilisateur));
	}
}
