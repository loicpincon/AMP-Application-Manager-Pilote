package application.manager.pilote.docker.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;

import application.manager.pilote.apimanager.modele.ApiManager;

@RestController
@RequestMapping("/docker")
@ApiManager("Docker")
public class DockerInfoController {

	@Autowired
	private DockerClient dockerClient;

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/containers")
	@ApiManager
	public Callable<ResponseEntity<List<Container>>> recuperer() {
		return () -> ResponseEntity.ok(dockerClient.listContainersCmd().exec());
	}

}
