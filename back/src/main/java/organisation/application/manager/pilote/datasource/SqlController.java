package organisation.application.manager.pilote.datasource;

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
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource/sql")
@ApiManager("SQL")
public class SqlController extends DefaultController {

	@Autowired
	private SqlService sqlService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idContainer}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> consulter(@PathVariable String idContainer) {
		return () -> ResponseEntity.ok(sqlService.consulter(idContainer));
	}
	

	/**
	 * 
	 * @return
	 */
	@GetMapping()
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> recuperer() {
		return () -> ResponseEntity.ok(sqlService.recuperer());
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
	@PostMapping(path = "/{idContainer}/bases")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> insererBase(@PathVariable String idContainer, @RequestParam String base) {
		return () -> ResponseEntity.ok(sqlService.inserer(idContainer, base));
	}

}
