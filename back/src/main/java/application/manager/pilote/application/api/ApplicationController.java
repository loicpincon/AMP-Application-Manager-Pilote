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
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.application.service.InstanceService;

@RestController
@RequestMapping("/applications")
@ApiManager("Application")
public class ApplicationController {

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
	public Callable<ResponseEntity<List<Application>>> recuperer() {
		return () -> ResponseEntity.ok(appService.recuperer());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	public Callable<ResponseEntity<Application>> ajouter(@RequestBody Application param) {
		return () -> ResponseEntity.ok(appService.inserer(param));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(path = "/{id}/{serveur}/instances")
	@ApiManager
	public Callable<ResponseEntity<Instance>> ajouterInstance(@PathVariable String id, @PathVariable Integer serveur,
			@RequestBody Instance instance) {
		return () -> ResponseEntity.ok(insService.ajouter(id, serveur, instance));
	}
}
