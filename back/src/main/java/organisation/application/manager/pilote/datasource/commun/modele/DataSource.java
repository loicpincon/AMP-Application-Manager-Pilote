package organisation.application.manager.pilote.datasource.commun.modele;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6308958573976352194L;

	@Id
	private String containerId;

	private String type;

	private String ip;

	private String port;

	private String user;

	private String password;

	private String idApp;

}
