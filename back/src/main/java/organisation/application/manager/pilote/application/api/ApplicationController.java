package organisation.application.manager.pilote.application.api;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.modele.Instance;
import organisation.application.manager.pilote.application.modele.Livrable;
import organisation.application.manager.pilote.application.modele.ParametreSeries;
import organisation.application.manager.pilote.application.service.ApplicationService;
import organisation.application.manager.pilote.application.service.InstanceService;
import organisation.application.manager.pilote.application.service.LivrableService;
import organisation.application.manager.pilote.commun.controller.DefaultController;
import organisation.application.manager.pilote.session.modele.Secured;
import organisation.application.manager.pilote.session.modele.SecuredLevel;

@RestController
@RequestMapping("/applications")
@ApiManager("Application")
public class ApplicationController extends DefaultController {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private InstanceService insService;

	@Autowired
	private LivrableService livrableService;

	/**
	 * 
	 * @return
	 */
	@GetMapping
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<Application>>> recuperer() {
		return () -> ResponseEntity.ok(appService.recuperer());
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idApp}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Application>> consulter(@PathVariable String idApp) {
		return () -> ResponseEntity.ok(appService.consulter(idApp));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(path = "/{idApp}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Application>> modifier(@PathVariable String idApp, @RequestBody Application app) {
		return () -> ResponseEntity.ok(appService.modifier(idApp, app));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{idUser}/applications")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<List<Application>>> recupererParUser(@PathVariable String idUser) {
		return () -> ResponseEntity.ok(appService.recupererParUser(idUser));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{idUser}/applications")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Application>> ajouter(@PathVariable String idUser, @RequestBody Application param) {
		return () -> ResponseEntity.ok(appService.inserer(idUser, param));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(path = "/{id}/{serveur}/instances")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Instance>> ajouterInstance(@PathVariable String id, @PathVariable Integer serveur) {
		return () -> ResponseEntity.ok(insService.ajouter(id, serveur));
	}

	/**
	 * 
	 * @return
	 */
	@PutMapping(path = "/{id}/{serveur}/parametres/{version}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<ParametreSeries>> modifierParametreSerie(@PathVariable String id,
			@PathVariable Integer serveur, @PathVariable String version, @RequestBody ParametreSeries parametre) {
		return () -> ResponseEntity.ok(appService.modifierParametreSerie(id, serveur, version, parametre));
	}

	/**
	 * 
	 * @return
	 */
	@PostMapping(path = "/{id}/{serveur}/parametres")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<ParametreSeries>> ajouterParametre(@PathVariable String id,
			@PathVariable Integer serveur, @RequestBody ParametreSeries param) {
		return () -> ResponseEntity.ok(appService.ajouterSerieParametre(id, serveur, param));
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/{id}/{serveur}/parametres/{version}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<ParametreSeries>> consulterParametre(@PathVariable String id,
			@PathVariable Integer serveur, @PathVariable String version) {
		return () -> ResponseEntity.ok(appService.consulterSerieParametre(id, serveur, version));
	}

	@PostMapping(path = "/{idApp}/versions")
	@ApiManager
	@Secured(level = SecuredLevel.UPLOAD_VERSION_APP)
	public Callable<ResponseEntity<Livrable>> uploadFileVersion(@PathVariable String idApp,
			@RequestParam("file") MultipartFile file, @RequestParam String version) {
		return () -> ResponseEntity.ok(insService.uploadFileVersion(file, idApp, version));
	}

	@GetMapping(path = "/{idApp}/livrables/{idVersion}")
	@ApiManager
	@Secured
	public Callable<ResponseEntity<Resource>> telechargerLivrable(HttpServletRequest request,
			@PathVariable String idApp, @PathVariable String idVersion) {

		return () -> {
			// Load file as Resource
			Resource resource = livrableService.telechargerVersion(idApp, idVersion);

			// Try to determine file's content type
			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			// Fallback to the default content type if type could not be determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		};

	}

}
