package application.manager.pilote.application.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarApplication extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520547287758028728L;

	private String versionWar;

	private String urlRepoNexs;

}
