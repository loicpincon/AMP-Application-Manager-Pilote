package organisation.application.manager.pilote.application.modele;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

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

	private String versionApplicationActuel;

	private String versionParametresActuel;

	private String libelleVersion;

	private String libelleEtatAction;

	private String url;

	private String port;

	private String etat;

	private List<UserAction> userActions = new ArrayList<>();

}
