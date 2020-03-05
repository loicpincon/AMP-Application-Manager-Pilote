package organisation.application.manager.pilote.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.datasource.modele.DataSource;
import organisation.application.manager.pilote.datasource.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.modele.DatasourceRepository;

@Service
public class DataSourceService {

	String test = "sh -c 'exec mysql -uroot -p\"$MYSQL_ROOT_PASSWORD\"'";

	@Autowired
	private DatasourceRepository datasourceRepo;

	@Autowired
	private MySqlService mysqlService;

	public List<DataSource> recuperer(String idApp) {
		return datasourceRepo.findByIdApp(idApp);
	}

	public DataSource ajouter(DataSourceEnum type, String idApp) {
		if (type.equals(DataSourceEnum.MYSQL)) {
			return mysqlService.ajouterDatasource(idApp);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, "Le type de datasource est inconnu");
		}
	}
}
