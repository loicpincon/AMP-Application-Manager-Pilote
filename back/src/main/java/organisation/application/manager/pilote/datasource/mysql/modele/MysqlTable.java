package organisation.application.manager.pilote.datasource.mysql.modele;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class MysqlTable extends BasicDataBean {

	/**
	* 
	*/
	private static final long serialVersionUID = -5473688815536793952L;

	@Mapping("TABLE_NAME")
	private String nom;

	@Mapping("TABLE_ROWS")
	private BigInteger nbeLignes;

	@Mapping("CREATE_TIME")
	private Date creation;
 
	@Mapping("UPDATE_TIME")
	private Date derniereModification;
}
