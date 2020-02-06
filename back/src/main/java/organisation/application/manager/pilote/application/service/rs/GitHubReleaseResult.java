package organisation.application.manager.pilote.application.service.rs;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class GitHubReleaseResult extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4021618439359720879L;
	
	private String name;

}
