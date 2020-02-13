package organisation.application.manager.pilote.application.modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class ParametreSeries extends BasicDataBean implements Comparable<ParametreSeries> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 643456810392429367L;

	private String version;

	private Date derniereModification;

	private List<Parametre> parametres = new ArrayList<>();

	private List<UserAction> userActions = new ArrayList<>();

	@Override
	public int compareTo(ParametreSeries o) {
		return this.version.compareTo(o.getVersion());
	}

}
