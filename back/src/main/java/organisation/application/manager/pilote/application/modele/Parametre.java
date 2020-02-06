package organisation.application.manager.pilote.application.modele;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class Parametre extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6358045793698289358L;

	private String cle;

	private String valeur;

}
