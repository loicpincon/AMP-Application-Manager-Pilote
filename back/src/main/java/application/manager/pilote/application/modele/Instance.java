package application.manager.pilote.application.modele;

import java.util.List;

import org.springframework.data.annotation.Id;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instance extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717238158810366397L;

	@Id
	private String id;

	private String containerId;
	
	private String libelle;

	private String url;

	private String etat;

	private List<UserAction> userActions;

}
