package organisation.application.manager.pilote.logs.modele;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class RechercheRessource extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1059141293149934444L;

	private List<EnvironnementLog> envs = new ArrayList<>();

}
