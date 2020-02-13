package organisation.application.manager.pilote.application.modele;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class UserAction extends BasicDataBean implements Comparable<UserAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2762516570935509964L;

	private String membre;

	private Date date;

	private String libelle;

	private String commentaire;

	private String version;

	private String status;

	@Override
	public int compareTo(UserAction o) {
		return this.date.compareTo(o.getDate());
	}
}
