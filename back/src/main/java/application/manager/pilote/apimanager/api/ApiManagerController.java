package application.manager.pilote.apimanager.api;

import java.util.Collection;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.helper.ApiManagerConfiguration;
import application.manager.pilote.apimanager.modele.Api;

@RestController
@RequestMapping("/api/map")
public class ApiManagerController {

	private static Collection<Api> apis;

	@Autowired
	private ApiManagerConfiguration apim;

	@Autowired
	private HttpServletRequest res;

	private Collection<Api> getApiMap() {
		if (apis == null) {
			apis = apim.apim(res).values();
		}
		return apis;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Callable<ResponseEntity<Collection<Api>>> recuperer(HttpServletRequest req) {
		return () -> {
			return ResponseEntity.ok(getApiMap());
		};
	}
}
