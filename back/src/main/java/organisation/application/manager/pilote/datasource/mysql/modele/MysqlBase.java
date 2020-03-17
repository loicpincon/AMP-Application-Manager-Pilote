package organisation.application.manager.pilote.datasource.mysql.modele;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class MysqlBase extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5969662905089879161L;

	private List<MysqlTable> tables = new ArrayList<>();

}
