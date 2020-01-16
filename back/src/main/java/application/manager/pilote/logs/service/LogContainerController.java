package application.manager.pilote.logs.service;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;

@RestController
@RequestMapping("/docker/logs")
@ApiManager("DockerLog")
public class LogContainerController {

	@Autowired
	private LogContainerService dockerLogsService;

	/**
	 * @return
	 */
	@GetMapping(path = "/{idContainer}")
	@ApiManager
	// @Secured
	public Callable<ResponseEntity<List<LogMessage>>> recuperer(@PathVariable String idContainer) {
		return () -> ResponseEntity.ok(dockerLogsService.getDockerLogs(idContainer));
	}

	/**
	 * @return
	 */
	@GetMapping(path = "/{idUser}/formualaire")
	@ApiManager
	// @Secured
	public Callable<ResponseEntity<RechercheRessource>> recupererInfoFormulaire(@PathVariable String idUser) {
		return () -> ResponseEntity.ok(dockerLogsService.recupererRechercheRessourceUser(idUser));
	}

}
