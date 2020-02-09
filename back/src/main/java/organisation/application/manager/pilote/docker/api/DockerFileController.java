package organisation.application.manager.pilote.docker.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.modele.ApiManager;
import organisation.application.manager.pilote.docker.modele.DockerFile;
import organisation.application.manager.pilote.docker.service.DockerFileService;
import organisation.application.manager.pilote.session.modele.Secured;

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
	@Secured
	public Callable<ResponseEntity<List<DockerFile>>> recuperer() {
		return () -> ResponseEntity.ok(dockerFileService.getAll());
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DockerFile>> creer(@RequestBody DockerFile param) {
		return () -> ResponseEntity.ok(dockerFileService.insert(param));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<DockerFile>> modifier(@RequestBody DockerFile param) {
		return () -> ResponseEntity.ok(dockerFileService.modifier(param));
	}

	/**
	 * 
	 * @return
	 */
	@DeleteMapping(path = "/{id}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Object>> supprimer(@PathVariable Integer id) {
		return () -> ResponseEntity.ok(dockerFileService.supprimer(id));
	}

}
