package application.manager.pilote.utilisateur.helper;

import org.springframework.stereotype.Component;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;
import application.manager.pilote.utilisateur.modele.DroitApplicatifLevel;

@Component
public class DroitApplicatifHelper {

	private static final String DEV = DroitApplicatifLevel.DEV.name();

	private static final String CP = DroitApplicatifLevel.CP.name();

	private static final String EXPERT = DroitApplicatifLevel.EXPERT.name();

	private static final String PROP = DroitApplicatifLevel.PROP.name();

	/**
	 * 
	 * @param da
	 * @param accessLevel
	 * @return
	 */
	public DroitApplicatif setDroitApplicatif(DroitApplicatif da, String accessLevel) {
		if (accessLevel.equals(DEV)) {
			setDevDroit(da);
		} else if (accessLevel.equals(CP)) {
			setCpDroit(da);
		} else if (accessLevel.equals(EXPERT)) {
			setExpertDroit(da);
		} else if (accessLevel.equals(PROP)) {
			setPropDroit(da);
		} else {
			throw new ApplicationException(400, "Ce niveau de droit est inconnu : " + accessLevel);
		}
		return da;
	}

	/**
	 * 
	 * @param da
	 */
	private void setDevDroit(DroitApplicatif da) {
		da.setRead(true);
		da.setPilote(false);
		da.setDelete(false);
		da.setUpdate(false);
		da.setAdmin(false);
	}

	/**
	 * 
	 * @param da
	 */
	private void setPropDroit(DroitApplicatif da) {
		da.setRead(true);
		da.setPilote(true);
		da.setDelete(true);
		da.setUpdate(true);
		da.setAdmin(true);
	}

	/**
	 * 
	 * @param da
	 */
	private void setExpertDroit(DroitApplicatif da) {
		da.setRead(true);
		da.setPilote(true);
		da.setDelete(false);
		da.setUpdate(true);
		da.setAdmin(true);
	}

	/**
	 * 
	 * @param da
	 */
	private void setCpDroit(DroitApplicatif da) {
		da.setRead(true);
		da.setPilote(true);
		da.setDelete(false);
		da.setUpdate(false);
		da.setAdmin(false);
	}

}
