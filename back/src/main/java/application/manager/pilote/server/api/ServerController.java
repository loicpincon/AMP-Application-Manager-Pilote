package application.manager.pilote.server.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@RestController
@RequestMapping("/servers")
@ApiManager("Server")
public class ServerController {

	@Autowired
	private ServerService serverService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager("recuperer")
	public Callable<ResponseEntity<List<Server>>> recuperer() {
		return () -> ResponseEntity.ok(serverService.recuperer());
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = GET)
	@ApiManager("consulter")
	public Callable<ResponseEntity<Server>> consulter(@PathVariable Integer id) {
		return () -> ResponseEntity.ok(serverService.consulter(id));
	}

	/**
	 * 
	 * @param server
	 * @return
	 */
	@PostMapping
	@ApiManager("inserer")
	public Callable<ResponseEntity<Server>> inserer(@RequestBody Server server) {
		return () -> ResponseEntity.ok(serverService.inserer(server));
	}

}
