package application.manager.pilote.utilisateur.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;
import application.manager.pilote.utilisateur.modele.Utilisateur;
import application.manager.pilote.utilisateur.service.UtilisateurService;

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
	@PutMapping(path = "/{id}")
	@ApiManager("modifier")
	public Callable<ResponseEntity<Utilisateur>> modifier(@PathVariable String id,
			@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.modifier(utilisateur));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{id}/droitsApplicatifs")
	@ApiManager
	public Callable<ResponseEntity<DroitApplicatif>> ajouterDroitApplicatifs(@PathVariable String id,
			@RequestBody DroitApplicatif droit, @RequestParam(required = true) String level) {
		return () -> ResponseEntity.ok(userService.ajouterDroitApplicatifs(id, droit, level));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{id}")
	@ApiManager("consulter")
	public Callable<ResponseEntity<Utilisateur>> consulter(@PathVariable String id) {
		return () -> ResponseEntity.ok(userService.consulter(id));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping()
	@ApiManager("recuperer")
	public Callable<ResponseEntity<List<Utilisateur>>> recuperer(@RequestParam(required = false) String idApp) {
		return () -> ResponseEntity.ok(userService.recuperer(idApp));
	}

}
