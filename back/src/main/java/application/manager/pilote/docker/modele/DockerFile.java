package application.manager.pilote.docker.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;

import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DockerFile extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8640942456195602818L;

	@Id
	private Integer id;

	private String name;

	private String file;

	private Boolean isPublic;
}
