package application.manager.pilote.session.api;

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
import application.manager.pilote.session.modele.UserSession;
import application.manager.pilote.session.service.SessionService;
import application.manager.pilote.session.service.UserSessionParam;

@RestController
@RequestMapping("/session")
@ApiManager("Session")
public class SessionController {

	@Autowired
	private SessionService sessionService;

	/**
	 * 
	 * @return
	 */
	@PostMapping
	@ApiManager("login")
	public Callable<ResponseEntity<UserSession>> login(@RequestBody UserSessionParam param) {
		return () -> ResponseEntity.ok(sessionService.connexion(param));
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping(path = "/{token}")
	@ApiManager("consulter")
	public Callable<ResponseEntity<UserSession>> consulter(@PathVariable String token) {
		return () -> ResponseEntity.ok(null);
	}

}
