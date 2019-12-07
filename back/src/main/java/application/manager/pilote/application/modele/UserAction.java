package application.manager.pilote.application.modele;

import java.util.Date;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

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

}
