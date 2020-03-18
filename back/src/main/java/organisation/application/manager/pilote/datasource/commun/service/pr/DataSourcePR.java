package organisation.application.manager.pilote.datasource.commun.service.pr;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class DataSourcePR extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3040789578310397793L;

	private String type;

	private String ip = "194.9.172.184";

	private String port = "";

	private String user = "root";

	private String password = "password";

	private String idApp;

}
