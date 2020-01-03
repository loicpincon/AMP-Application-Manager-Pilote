package application.manager.pilote.docker.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.docker.modele.Container;
import application.manager.pilote.docker.service.DockerContainerService;
import application.manager.pilote.docker.service.pr.ContainerParam;

@RestController
@RequestMapping("/docker")
@ApiManager("Docker")
public class DockerInfoController {

	@Autowired
	private DockerContainerService dockerService;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/containers")
	@ApiManager
	public Callable<ResponseEntity<List<Container>>> recuperer() {
		return () -> ResponseEntity.ok(dockerService.getContainers());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/containers")
	@ApiManager
	public Callable<ResponseEntity<Container>> creer(ContainerParam param) {
		return () -> ResponseEntity.ok(dockerService.createContainer(param));
	}

}
