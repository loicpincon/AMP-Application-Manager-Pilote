package organisation.application.manager.pilote.datasource.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.service.MySqlService;
import organisation.application.manager.pilote.datasource.service.pr.RequeteParam;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource/mysql")
@ApiManager("mysql")
public class SqlController extends DefaultController {

	@Autowired
	private MySqlService sqlService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idApp}/{idContainer}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> consulter(@PathVariable String idApp, @PathVariable String idContainer) {
		return () -> ResponseEntity.ok(sqlService.consulter(idContainer));
	}

	

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{idContainer}/requete")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> executerRequete(@PathVariable String idContainer,
			@RequestBody RequeteParam param) {
		return () -> ResponseEntity.ok(sqlService.executeRequete(idContainer, param));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{idApp}/{idContainer}/bases")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> insererBase(@PathVariable String idApp, @PathVariable String idContainer,
			@RequestParam String base) {
		return () -> ResponseEntity.ok(sqlService.inserer(idApp, idContainer, base));
	}

	

}
