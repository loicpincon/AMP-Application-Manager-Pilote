package organisation.application.manager.pilote.datasource.mysql.modele;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class BaseDeDonneeRessource extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6070932449405552982L;

	private String nom;

	private List<MysqlTable> tables = new ArrayList<>();

}
