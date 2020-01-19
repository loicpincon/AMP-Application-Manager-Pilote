package application.manager.pilote.apimanager.api;

import java.util.Collection;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.helper.ApiManagerConfiguration;
import application.manager.pilote.apimanager.modele.Api;
import application.manager.pilote.apimanager.modele.ApiManager;

@RestController
@RequestMapping("/api/map")
@ApiManager("Manager")
public class ApiManagerController {

	@Autowired
	private ApiManagerConfiguration apim;

	@GetMapping
	public Callable<ResponseEntity<Collection<Api>>> recuperer(HttpServletRequest req) {
		return () -> ResponseEntity.ok(apim.getApiManager(req).values());
	}
}
