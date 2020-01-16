package application.manager.pilote.logs.service;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceLog extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4282665841918268388L;

	private String id;
	private String libelle;

}
