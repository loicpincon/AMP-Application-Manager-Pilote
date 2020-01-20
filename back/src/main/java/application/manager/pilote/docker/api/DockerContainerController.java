package application.manager.pilote.docker.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.docker.modele.Container;
import application.manager.pilote.docker.service.DockerContainerService;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.session.modele.Secured;

@RestController
@RequestMapping("/docker")
@ApiManager("Docker")
public class DockerContainerController {

	@Autowired
	private DockerContainerService dockerService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/instances")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<Container>>> recuperer() {
		return () -> ResponseEntity.ok(dockerService.getInstances());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/containers")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<com.github.dockerjava.api.model.Container>>> recupererContainers() {
		return () -> ResponseEntity.ok(dockerService.getContainers());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/instances")
	@ApiManager
	@Secured
	@Transactional(timeout = 1)
	public Callable<ResponseEntity<Instance>> creer(@RequestBody ContainerParam param) {
		return () -> ResponseEntity.ok(dockerService.createContainer(param));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/application/{idApp}/{idServer}/containers/{id}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Instance>> manage(@PathVariable String idApp, @PathVariable Integer idServer,
			@PathVariable String id, @RequestParam String action) {
		return () -> ResponseEntity.ok(dockerService.manage(idApp, idServer, id, action));
	}

}
