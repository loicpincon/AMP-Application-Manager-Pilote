package organisation.application.manager.pilote.datasource.mysql.modele;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceItem;

@Getter
@Setter
@Document
public class MysqlDataSource extends DataSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2268775620787314888L;

	private List<DataSourceItem> bases = new ArrayList<>();

}
