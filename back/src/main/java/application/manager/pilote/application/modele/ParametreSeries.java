package application.manager.pilote.application.modele;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class ParametreSeries extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 643456810392429367L;

	private String version;

	private Date derniereModification;

	private Map<String, String> parametres;

	private List<UserAction> userActions;

}
