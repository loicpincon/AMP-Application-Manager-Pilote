package application.manager.pilote.application.api;

import static application.manager.pilote.application.modele.ApplicationType.getApplicationTypes;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.session.modele.Secured;
import organisation.apimanager.modele.ApiManager;

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
	@Secured
	public Callable<ResponseEntity<List<String>>> recupererTypes() {
		return () -> ResponseEntity.ok(Arrays.asList(getApplicationTypes()));
	}

}
