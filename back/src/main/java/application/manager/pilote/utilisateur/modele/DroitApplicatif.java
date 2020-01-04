package application.manager.pilote.utilisateur.modele;

import java.util.Date;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroitApplicatif extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054626476133040843L;

	private String applicationId;

	private Date date;

	private boolean read;

	private boolean update;

	private boolean delete;

	private boolean pilote;

	private boolean admin;

}
