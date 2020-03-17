package organisation.application.manager.pilote.datasource.mysql.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.mysql.modele.BaseDeDonneeRessource;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.service.BaseDeDonneService;
import organisation.application.manager.pilote.datasource.mysql.service.BaseDeDonneeRessourcePR;
import organisation.application.manager.pilote.session.modele.Secured;
import organisation.application.manager.pilote.session.modele.SecuredLevel;

@RestController
@RequestMapping("/datasource/mysql")
@ApiManager("mysql2")
public class MysqlController extends DefaultController {

	@Autowired
	private BaseDeDonneService baseDonneesService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<MysqlDataSource>>> recupererServeur() {
		return () -> ResponseEntity.ok(baseDonneesService.recupererServeur());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{id}")
	@ApiManager
	@Secured(level = SecuredLevel.CONSULTER_BASE_ADMIN)
	public Callable<ResponseEntity<List<DataSourceItem>>> recupererBase(@PathVariable String id) {
		return () -> ResponseEntity.ok(baseDonneesService.recupererBase(id));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{id}/bases/{name}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<BaseDeDonneeRessource>> consulter(@PathVariable String id,
			@PathVariable String name) {
		return () -> ResponseEntity.ok(baseDonneesService.consulter(id, name));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<BaseDeDonneeRessource>> inserer(@RequestBody BaseDeDonneeRessourcePR body) {
		return () -> ResponseEntity.ok(baseDonneesService.inserer(body));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{id}/bases")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DataSourceItem>> insererBase(@PathVariable String id,
			@RequestBody DataSourceItem body) {
		return () -> ResponseEntity.ok(baseDonneesService.insererBase(id, body));
	}

}
