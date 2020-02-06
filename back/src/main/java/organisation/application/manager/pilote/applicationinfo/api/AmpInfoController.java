package organisation.application.manager.pilote.applicationinfo.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.modele.ApiManager;
import organisation.application.manager.pilote.applicationinfo.modele.ApplicationInfoRessource;
import organisation.application.manager.pilote.applicationinfo.service.ApplicationInfoService;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/api/infos")
@ApiManager("Information")
public class AmpInfoController extends DefaultController {

	@Autowired
	private ApplicationInfoService appInfoService;

	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<ApplicationInfoRessource>> recuperer() {
		return () -> ResponseEntity.ok(appInfoService.get());
	}
}
