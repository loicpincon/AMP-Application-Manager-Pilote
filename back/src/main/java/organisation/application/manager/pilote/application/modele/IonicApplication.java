package organisation.application.manager.pilote.application.modele;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IonicApplication extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4013316352075690620L;

	private String coderepositoryUrl;

	private Boolean repoUser;

	private String repoPass;

	private String userProprietaire;

	private String nomRepository;

}
