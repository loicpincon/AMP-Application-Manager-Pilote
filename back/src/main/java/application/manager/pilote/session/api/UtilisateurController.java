package application.manager.pilote.session.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@PostMapping
	@ApiManager("ajouter")
	public Callable<ResponseEntity<Utilisateur>> ajouter(@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.inserer(utilisateur));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(value = "/{id}")
	@ApiManager("modifier")
	public Callable<ResponseEntity<Utilisateur>> modifier(@PathVariable String id,
			@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.modifier(id, utilisateur));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(value = "/{id}")
	@ApiManager("consulter")
	public Callable<ResponseEntity<Utilisateur>> consulter(@PathVariable String id) {
		return () -> ResponseEntity.ok(userService.consulter(id));
	}
}
