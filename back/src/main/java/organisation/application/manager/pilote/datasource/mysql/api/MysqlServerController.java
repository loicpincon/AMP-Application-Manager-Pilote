package organisation.application.manager.pilote.datasource.mysql.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.commun.service.pr.DataSourcePR;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.service.MysqlBaseService;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource/mysql")
@ApiManager("mysql.server")
public class MysqlServerController extends DefaultController {

	@Autowired
	private MysqlBaseService baseDonneesService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<MysqlDataSource>>> recuperer(@RequestParam(required = false) String idApp) {
		return () -> ResponseEntity.ok(baseDonneesService.recupererServeur(idApp));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<MysqlDataSource>> inserer(@RequestBody DataSourcePR body) {
		return () -> ResponseEntity.ok(baseDonneesService.inserer(body));
	}

}
