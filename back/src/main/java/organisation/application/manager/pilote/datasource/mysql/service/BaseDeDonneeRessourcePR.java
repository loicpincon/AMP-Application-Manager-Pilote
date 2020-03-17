package organisation.application.manager.pilote.datasource.mysql.service;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class BaseDeDonneeRessourcePR extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5593368302118884472L;

	private String type;

	private String ip;

	private String port;

	private String user;

	private String password;

	private String idApp;
}
