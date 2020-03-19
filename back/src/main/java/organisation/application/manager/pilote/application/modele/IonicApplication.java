package organisation.application.manager.pilote.application.modele;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IonicApplication extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4013316352075690620L;

	private String repositoryUrl;

	private String repoUser;

	private String repoPass;

	private List<String> branches;

}
