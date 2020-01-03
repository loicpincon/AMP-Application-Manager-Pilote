package application.manager.pilote.docker.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.docker.modele.DockerFile;
import application.manager.pilote.docker.service.DockerFileService;
import application.manager.pilote.docker.service.pr.DockerFileParam;

@RestController
@RequestMapping("/dockerfile")
@ApiManager("Dockerfile")
public class DockerFileController {
	@Autowired
	private DockerFileService dockerFileService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	public Callable<ResponseEntity<List<DockerFile>>> recuperer() {
		return () -> ResponseEntity.ok(dockerFileService.getAll());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	public Callable<ResponseEntity<DockerFile>> creer(@RequestBody DockerFileParam param) {
		return () -> ResponseEntity.ok(dockerFileService.insert(param));
	}
}
