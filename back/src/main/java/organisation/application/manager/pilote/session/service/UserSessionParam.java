package organisation.application.manager.pilote.session.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
@NoArgsConstructor
public class UserSessionParam extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9192569367461485664L;

	private String login;

	private String password;

}
