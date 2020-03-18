package organisation.application.manager.pilote.datasource.mysql.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.mysql.service.MysqlBaseService;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource/mysql")
@ApiManager("mysql.table")
public class MysqlTableController extends DefaultController {

	@Autowired
	private MysqlBaseService baseDonneesService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{id}/bases/{idbase}/tables")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> recuperer(@PathVariable String id, @PathVariable String idbase) {
		return () -> ResponseEntity.ok(baseDonneesService.getTablesOfBases(id, idbase));
	}

}
