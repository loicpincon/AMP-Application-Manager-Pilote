package application.manager.pilote.application.api;

import static application.manager.pilote.application.modele.ApplicationType.APPLICATIONS_TYPE;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.repository.ApplicationRepository;

@RestController
@RequestMapping("/applications")
@ApiManager("Application")
public class ApplicationController {

	@Autowired
	private ApplicationRepository appRepo;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager("recuperer")
	public Callable<ResponseEntity<List<Application>>> recuperer() {
		return () -> ResponseEntity.ok(appRepo.findAll());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/types")
	@ApiManager("recupererTypes")
	public Callable<ResponseEntity<List<String>>> recupererTypes() {
		return () -> ResponseEntity.ok(Arrays.asList(APPLICATIONS_TYPE));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager("ajouter")
	public Callable<ResponseEntity<Application>> ajouter(@RequestBody Application param) {
		return () -> ResponseEntity.ok(appRepo.insert(param));
	}
}
