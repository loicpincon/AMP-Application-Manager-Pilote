package organisation.application.manager.pilote.datasource.commun.api;

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
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.commun.service.DataSourceService;
import organisation.application.manager.pilote.datasource.commun.service.pr.DataSourcePR;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.service.MysqlBaseService;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource")
@ApiManager("datasource")
public class DataSourceContoller extends DefaultController {

	@Autowired
	private DataSourceService datasourceService;

	@Autowired
	private MysqlBaseService mysqlBaseService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/type")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DataSourceEnum[]>> recupererType() {
		return () -> ResponseEntity.ok(DataSourceEnum.values());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<DataSource>>> recuperer(@RequestParam(required = false) String idApp) {
		return () -> ResponseEntity.ok(datasourceService.recuperer(idApp));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DataSource>> ajouter(@RequestBody DataSourcePR param) {
		return () -> ResponseEntity.ok(datasourceService.ajouter(param));
	}

}
