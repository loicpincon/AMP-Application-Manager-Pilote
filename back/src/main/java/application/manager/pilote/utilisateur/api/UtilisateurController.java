package application.manager.pilote.utilisateur.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.session.modele.Secured;
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
	 * @return
	 */
	@PostMapping
	@ApiManager("ajouter")
	public Callable<ResponseEntity<Utilisateur>> ajouter(@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.inserer(utilisateur));
	}

	/**
	 * @return
	 */
	@PutMapping(path = "/{id}")
	@ApiManager("modifier")
	@Secured
	public Callable<ResponseEntity<Utilisateur>> modifier(@PathVariable String id,
			@RequestBody Utilisateur utilisateur) {
		return () -> ResponseEntity.ok(userService.modifier(id, utilisateur));
	}

	/**
	 * @return
	 */
	@PostMapping(path = "/{id}/droitsApplicatifs")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DroitApplicatif>> ajouterDroitApplicatifs(@PathVariable String id,
			@RequestBody DroitApplicatif droit, @RequestParam(required = false) String level,
			@RequestParam(required = false) Boolean delete) {
		return () -> ResponseEntity.ok(userService.ajouterDroitApplicatifs(id, droit, level, delete));
	}

	/**
	 * @return
	 */
	@GetMapping(path = "/{id}")
	@ApiManager("consulter")
	@Secured
	public Callable<ResponseEntity<Utilisateur>> consulter(@PathVariable String id) {
		return () -> ResponseEntity.ok(userService.consulter(id));
	}

	/**
	 * @return
	 */
	@GetMapping()
	@ApiManager("recuperer")
	@Secured
	public Callable<ResponseEntity<List<Utilisateur>>> recuperer(@RequestParam(required = false) String idApp,
			@RequestParam(required = false) String keyword) {
		return () -> ResponseEntity.ok(userService.recuperer(idApp, keyword));
	}

	@PutMapping(path = "/{id}/image")
	@ApiManager("uploadImage")
	public Callable<ResponseEntity<?>> multiUploadFileModel(@PathVariable String id,
			@RequestParam("file") MultipartFile file) {
		return () -> ResponseEntity.ok(userService.setPhotoToMembre(id, file));
	}

	@GetMapping(path = "/{id}/image")
	@ApiManager("getImage")
	public Callable<ResponseEntity<ByteArrayResource>> getPhoto(@PathVariable String id, Model model) {
		Utilisateur membre = userService.consulter(id);
		if (membre.getImage() == null) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "Aucune image pour ce membre");
		}
		return () -> ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=")
				.contentType(MediaType.IMAGE_JPEG).contentLength(membre.getImage().getData().length)
				.body(new ByteArrayResource(membre.getImage().getData()));
	}

}
