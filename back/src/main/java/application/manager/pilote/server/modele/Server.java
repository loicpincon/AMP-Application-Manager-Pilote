package application.manager.pilote.server.modele;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server {

	@Id
	public Integer id;

	private String nom;

	private String ip;

	private String dns;

}
