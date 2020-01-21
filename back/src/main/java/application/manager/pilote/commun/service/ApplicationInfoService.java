package application.manager.pilote.commun.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.modele.ApplicationInfoRessource;

@Service
public class ApplicationInfoService {

	protected static final Log LOG = LogFactory.getLog(ApplicationInfoService.class);

	public ApplicationInfoRessource get() {
		ApplicationInfoRessource appinfo = new ApplicationInfoRessource();
		appinfo.setVersion(getPomVersion());
		return appinfo;
	}

	/**
	 * recupere la version de l'application
	 * 
	 * @return
	 */
	private String getPomVersion() {
		return this.getClass().getPackage().getImplementationVersion();
	}

}
