package application.manager.pilote.server.modele;

import org.springframework.data.annotation.Id;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server extends BasicDataBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1046908704673727737L;

	@Id
	public Integer id;

	private String nom;

	private String ip;

	private String dns;

}
