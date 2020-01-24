package application.manager.pilote.application.service.rs;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubReleaseResult extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4021618439359720879L;
	
	private String name;

}
