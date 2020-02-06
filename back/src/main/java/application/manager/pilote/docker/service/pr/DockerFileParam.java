package application.manager.pilote.docker.service.pr;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DockerFileParam extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8878321333091605509L;

	private String file;

	private String name;

	private Boolean isPublic = true;
}
