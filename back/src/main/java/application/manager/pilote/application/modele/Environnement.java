package application.manager.pilote.application.modele;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class Environnement extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7621036045425502694L;

	private List<ParametreSeries> parametres;

	private List<Instance> instances;

}
