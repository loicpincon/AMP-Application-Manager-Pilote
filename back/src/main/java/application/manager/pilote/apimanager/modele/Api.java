package application.manager.pilote.apimanager.modele;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Api implements Serializable {

	private static final long serialVersionUID = -6498181874756390953L;

	/**
	 * Cle de l'api au format <nom-composant>.<libelle-Methode>
	 */
	private String key;

	/**
	 * URI d'acces a l'API
	 */
	private String uri;

	/**
	 * URL d'acces a l'API
	 */
	private String url;

	/**
	 * Methode d'appel de l'API GET, POST, PUT, DELETE
	 */
	private String verbe;

	/**
	 * 
	 */
	private String serveur;

}
