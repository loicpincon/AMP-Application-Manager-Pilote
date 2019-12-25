package application.manager.pilote.session.modele;

import java.util.List;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSession extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3589374037449861898L;

	private String token;

	private List<DroitApplicatif> rights;
}
