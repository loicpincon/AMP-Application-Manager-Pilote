package organisation.application.manager.pilote.datasource.service.pr;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class RequeteParam extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2118765681461979943L;

	private String requete;
	
	private String base;
	
	private String type;

}
