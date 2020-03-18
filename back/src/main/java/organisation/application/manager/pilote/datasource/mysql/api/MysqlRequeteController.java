package organisation.application.manager.pilote.datasource.mysql.api;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.commun.service.pr.RequeteParam;
import organisation.application.manager.pilote.datasource.mysql.service.MySqlService;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource/mysql")
@ApiManager("mysql.requete")
public class MysqlRequeteController extends DefaultController {

	@Autowired
	private MySqlService sqlService;

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{id}/requete")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<?>> executer(@PathVariable String id, @RequestBody RequeteParam param) {
		return () -> ResponseEntity.ok(sqlService.executeRequete(id, param));
	}

}
