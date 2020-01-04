package application.manager.pilote.utilisateur.helper;

import org.springframework.stereotype.Component;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;

@Component
public class DroitApplicatifHelper {

	private static final String DEV = "DEV";

	private static final String CP = "CP";

	private static final String EXPERT = "EXPERT";

	private static final String PROP = "PROP";

	/**
	 * 
	 * @param da
	 * @param accessLevel
	 * @return
	 */
	public DroitApplicatif setDroitApplicatif(DroitApplicatif da, String accessLevel) {
		switch (accessLevel) {
		case DEV:
			setDevDroit(da);
			break;
		case CP:
			setCpDroit(da);
			break;
		case EXPERT:
			setExpertDroit(da);
			break;
		case PROP:
			setPropDroit(da);
			break;
		default:
			throw new ApplicationException(400, "Ce niveau de droit est inconnu");
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
