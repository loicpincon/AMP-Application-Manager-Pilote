package organisation.application.manager.pilote.datasource.commun.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceItem extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1842248132702613591L;

	private String name;

	private String user;

	private String password;

}
