package application.manager.pilote.docker.service.pr;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerParam extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7201337464936970424L;

	private String idApplicationCible;

	private String idInstanceCible;

	private Integer idServeurCible;

	private String version;
}