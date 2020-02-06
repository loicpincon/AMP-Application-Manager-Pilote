package organisation.application.manager.pilote.logs.modele;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class EnvironnementLog extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115405119729811136L;

	private Integer idEnv;

	private String libelle;

	private List<ApplicationLog> apps = new ArrayList<>();

}
