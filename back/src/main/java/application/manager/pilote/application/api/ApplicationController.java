package application.manager.pilote.application.api;

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
import org.springframework.web.multipart.MultipartFile;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.Livrable;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.application.service.InstanceService;
import application.manager.pilote.commun.controller.DefaultController;
import application.manager.pilote.session.modele.Secured;
import application.manager.pilote.session.modele.SecuredLevel;

@RestController
@RequestMapping("/applications")
@ApiManager("Application")
public class ApplicationController extends DefaultController {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private InstanceService insService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<Application>>> recuperer() {
		return () -> ResponseEntity.ok(appService.recuperer());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idApp}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Application>> consulter(@PathVariable String idApp) {
		return () -> ResponseEntity.ok(appService.consulter(idApp));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idUser}/applications")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<Application>>> recupererParUser(@PathVariable String idUser) {
		return () -> ResponseEntity.ok(appService.recupererParUser(idUser));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{idUser}/applications")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Application>> ajouter(@PathVariable String idUser, @RequestBody Application param) {
		return () -> ResponseEntity.ok(appService.inserer(idUser, param));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(path = "/{id}/{serveur}/instances")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Instance>> ajouterInstance(@PathVariable String id, @PathVariable Integer serveur,
			@RequestBody Instance instance) {
		return () -> ResponseEntity.ok(insService.ajouter(id, serveur, instance));
	}

	@PostMapping(path = "/{idApp}/versions")
	@ApiManager
	@Secured(level = SecuredLevel.UPLOAD_VERSION_APP)
	public Callable<ResponseEntity<Livrable>> uploadFileVersion(@PathVariable String idApp,
			@RequestParam("file") MultipartFile file, @RequestParam String version) {
		return () -> ResponseEntity.ok(insService.uploadFileVersion(file, idApp, version));
	}

}
