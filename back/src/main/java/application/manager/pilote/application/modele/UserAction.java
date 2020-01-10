package application.manager.pilote.application.modele;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class UserAction extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2762516570935509964L;

	private String membre;

	private Date date;

	private String commentaire;
	
	private String version;
	
	private String status;

}
