package organisation.application.manager.pilote.application.modele;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class Environnement extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7621036045425502694L;

	private List<ParametreSeries> parametres = new ArrayList<>();

	private List<Instance> instances = new ArrayList<>();

}
