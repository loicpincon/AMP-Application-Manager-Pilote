package organisation.application.manager.pilote.datasource.mysql.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServeurParam extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3452623587751995353L;

	public ServeurParam(DataSource datasource) {

	}

	private String datasource;

	private String host;

	private String port;

	private String login;

	private String password;

	public String getUrl() {
		return "jdbc:mysql://" + host + ":" + port + "/" + datasource;
	}
}
