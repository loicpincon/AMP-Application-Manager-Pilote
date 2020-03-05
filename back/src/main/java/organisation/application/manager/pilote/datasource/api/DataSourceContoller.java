package organisation.application.manager.pilote.datasource.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.datasource.modele.DataSource;
import organisation.application.manager.pilote.datasource.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.service.DataSourceService;
import organisation.application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/datasource")
@ApiManager("datasource")
public class DataSourceContoller extends DefaultController {

	@Autowired
	private DataSourceService datasourceService;

	/**
	 * 
	 * @return
	 */
	@GetMapping()
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DataSourceEnum[]>> recuperer() {
		return () -> ResponseEntity.ok(DataSourceEnum.values());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idApp}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<DataSource>>> recupererParApp(@PathVariable String idApp) {
		return () -> ResponseEntity.ok(datasourceService.recuperer(idApp));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{idApp}/datasources")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DataSource>> ajouterDatasource(@PathVariable String idApp,
			@RequestParam String type) {
		return () -> ResponseEntity.ok(datasourceService.ajouter(DataSourceEnum.valueOf(type), idApp));
	}

}
