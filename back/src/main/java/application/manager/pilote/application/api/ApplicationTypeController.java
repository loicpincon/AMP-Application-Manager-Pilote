package application.manager.pilote.application.api;

import static application.manager.pilote.application.modele.ApplicationType.APPLICATIONS_TYPE;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;

@RestController
@RequestMapping("/application-types")
@ApiManager("ApplicationType")
public class ApplicationTypeController {

	/**
	 * 
	 * @return
	 */
	@GetMapping()
	@ApiManager("recuperer")
	public Callable<ResponseEntity<List<String>>> recupererTypes() {
		return () -> ResponseEntity.ok(Arrays.asList(APPLICATIONS_TYPE));
	}

}
