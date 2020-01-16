package application.manager.pilote.logs.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.logs.modele.LogMessage;
import application.manager.pilote.logs.modele.RechercheRessource;
import application.manager.pilote.logs.service.LogContainerService;

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
	public Callable<ResponseEntity<List<LogMessage>>> recuperer(@PathVariable String idContainer,
			@RequestParam(required = false) String debut, @RequestParam(required = false) String fin) {
		return () -> {
			Date debutD = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(debut);
			Date finD = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(fin);

			return ResponseEntity.ok(dockerLogsService.getDockerLogs(idContainer, debutD, finD));
		};
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
