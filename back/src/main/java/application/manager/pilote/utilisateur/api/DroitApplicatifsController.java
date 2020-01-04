package application.manager.pilote.utilisateur.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.apimanager.modele.ApiManager;
import application.manager.pilote.commun.modele.BasicDataBean;
import application.manager.pilote.utilisateur.modele.DroitApplicatifLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping("/droitapplicatifs")
@ApiManager("DroitApplicatifs")
public class DroitApplicatifsController {

	/**
	 * 
	 * @return
	 */
	@GetMapping()
	@ApiManager
	public Callable<ResponseEntity<List<DroitApplicatifLevelRessource>>> recuperer() {
		return () -> {
			List<DroitApplicatifLevelRessource> listDroitApplicatifLevelRessource = new ArrayList<>();
			for (DroitApplicatifLevel droit : DroitApplicatifLevel.values()) {
				listDroitApplicatifLevelRessource
						.add(new DroitApplicatifLevelRessource(droit.name(), droit.getLibelle()));
			}
			return ResponseEntity.ok(listDroitApplicatifLevelRessource);
		};
	}

	@Getter
	@AllArgsConstructor
	private class DroitApplicatifLevelRessource extends BasicDataBean {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5150486714929802552L;

		private String id;

		private String libelle;
	}
}
