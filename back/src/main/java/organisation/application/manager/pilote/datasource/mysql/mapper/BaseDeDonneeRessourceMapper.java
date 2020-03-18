package organisation.application.manager.pilote.datasource.mysql.mapper;

import org.springframework.stereotype.Component;

import organisation.application.manager.pilote.datasource.commun.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.mysql.modele.BaseDeDonneeRessource;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;

@Component
public class BaseDeDonneeRessourceMapper {

	public BaseDeDonneeRessource map(MysqlDataSource param, DataSourceItem item) {
		BaseDeDonneeRessource ressource = new BaseDeDonneeRessource();
		if (item != null) {
			ressource.setNom(item.getName());
		}
		return ressource;
	}

}
