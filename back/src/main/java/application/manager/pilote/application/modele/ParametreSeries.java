package application.manager.pilote.application.modele;

import java.util.Date;
import java.util.List;
import java.util.Map;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

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
