package organisation.application.manager.pilote.datasource;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.DefaultService;

@Service
public class SqlService extends DefaultService {

	protected static final Log LOG = LogFactory.getLog(SqlService.class);

	@Autowired
	private DatasourceRepository datasourceRepo;

	public Object executeRequete(String containerId, RequeteParam param) {
		Optional<DataSource> datasourceO = datasourceRepo.findById(containerId);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();
			MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(), datasource.getUser(),
					datasource.getPassword(), param.getBase());
			if (param.getType().equals("select")) {
				return mysql.execute(param.getRequete());
			} else {
				mysql.executeRequeteSystem(param.getRequete());
				return mysql.execute("SELECT * from " + param.getBase());
			}
		}
		throw new ApplicationException(400, "Container inconnu");
	}

	public Object inserer(String containerId, String name) {

		// INSERER DANS BASE

		Optional<DataSource> datasourceO = datasourceRepo.findById(containerId);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();

			datasource.getBases().add(name);
			datasourceRepo.save(datasource);
			MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(), datasource.getUser(),
					datasource.getPassword(), "mysql");
			mysql.executeRequeteSystem("CREATE DATABASE " + name + " ;");
			return datasource;
		}
		// EXECUTER REQUETE

		throw new ApplicationException(400, "Container inconnu");
	}

	public Object consulter(String idContainer) {
		Optional<DataSource> datasourceO = datasourceRepo.findById(idContainer);
		if (datasourceO.isPresent()) {
			return datasourceO.get();
		}
		throw new ApplicationException(400, "Container inconnu");

	}

	public Object recuperer() {
		return datasourceRepo.findAll();
	}

}
