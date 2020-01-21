package application.manager.pilote.utilisateur.modele;

import java.util.List;

import org.springframework.data.annotation.Id;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Utilisateur extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1214594229396692377L;

	@Id
	private String login;

	private String email;

	private String password;

	private String token;

	private String nom;

	private String prenom;

	private List<DroitApplicatif> rights;

}
