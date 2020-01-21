package application.manager.pilote.commun.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.commun.controller.DefaultController;
import application.manager.pilote.commun.modele.ApplicationInfoRessource;
import application.manager.pilote.commun.service.ApplicationInfoService;
import application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/api/infos")
@ApiManager("Information")
public class ApplicationInfoController extends DefaultController {

	@Autowired
	private ApplicationInfoService appInfoService;

	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<ApplicationInfoRessource>> recuperer() {
		return () -> ResponseEntity.ok(appInfoService.get());
	}
}
