package application.manager.pilote.application.modele;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AngularApplication extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4013316352075690620L;

	private String versionAngular;

	private Boolean isBuilder;

	private String baseLocation;

}
