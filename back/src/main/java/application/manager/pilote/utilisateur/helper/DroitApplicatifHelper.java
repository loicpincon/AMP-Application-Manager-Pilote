package application.manager.pilote.utilisateur.helper;

import java.util.Date;

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
		da.setDate(new Date());
		if (accessLevel.equals(DEV)) {
			da.setLevel(DroitApplicatifLevel.DEV);
		} else if (accessLevel.equals(CP)) {
			da.setLevel(DroitApplicatifLevel.CP);
		} else if (accessLevel.equals(EXPERT)) {
			da.setLevel(DroitApplicatifLevel.EXPERT);
		} else if (accessLevel.equals(PROP)) {
			da.setLevel(DroitApplicatifLevel.PROP);
		} else {
			throw new ApplicationException(400, "Ce niveau de droit est inconnu : " + accessLevel);
		}
		return da;
	}

}
