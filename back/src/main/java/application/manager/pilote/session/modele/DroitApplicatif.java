package application.manager.pilote.session.modele;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroitApplicatif extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054626476133040843L;

	private String applicationId;

	private Boolean read;

	private Boolean update;

	private Boolean delete;

	private Boolean pilote;

}
