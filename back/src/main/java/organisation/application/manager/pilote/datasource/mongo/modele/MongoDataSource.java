package organisation.application.manager.pilote.datasource.mongo.modele;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;

@Getter
@Setter
@Document
public class MongoDataSource extends DataSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1498305560175704476L;

}
