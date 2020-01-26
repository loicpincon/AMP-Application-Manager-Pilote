package application.manager.pilote.application.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import application.manager.pilote.commun.modele.BasicDataBean;
import application.manager.pilote.docker.modele.DockerFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({ @Type(name = ApplicationType.BASH, value = BashApplication.class),
		@Type(name = ApplicationType.WAR, value = WarApplication.class),
		@Type(name = ApplicationType.NODEJS, value = NodeJsApplication.class),
		@Type(name = ApplicationType.ANGULAR, value = AngularApplication.class) })
public class Application extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4478928365182068579L;

	@Id
	private String id;

	private String name;

	private String type;

	private Map<Integer, Environnement> environnements = new HashMap<>();

	private DockerFile dockerfile;


	private String baseName;

	private List<Livrable> livrables = new ArrayList<>();

}
