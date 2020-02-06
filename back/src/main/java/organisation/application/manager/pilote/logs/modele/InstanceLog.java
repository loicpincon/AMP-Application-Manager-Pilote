package organisation.application.manager.pilote.logs.modele;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class InstanceLog extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4282665841918268388L;

	private String id;
	private String libelle;
	private String etat;

}
