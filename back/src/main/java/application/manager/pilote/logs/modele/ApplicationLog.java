package application.manager.pilote.logs.modele;

import java.util.ArrayList;
import java.util.List;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationLog extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6665790624933037687L;

	private String id;

	private String name;

	private List<InstanceLog> instances = new ArrayList<>();
}
