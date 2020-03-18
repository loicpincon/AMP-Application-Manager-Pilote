package organisation.application.manager.pilote.datasource.mysql.service;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.service.pr.RequeteParam;
import organisation.application.manager.pilote.datasource.mysql.helper.MySqlConnector;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.repository.MySqlDataSourceRepository;

@Service
public class MySqlService extends DefaultService {

	protected static final Log LOG = LogFactory.getLog(MySqlService.class);

	@Autowired
	private MySqlDataSourceRepository datasourceRepo;

	public Object executeRequete(String containerId, RequeteParam param) {
		Optional<MysqlDataSource> datasourceO = datasourceRepo.findById(containerId);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();
			MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(), datasource.getUser(),
					datasource.getPassword(), param.getBase());
			if (param.getType().equals("select")) {
				return mysql.execute(param.getRequete());
			} else {
				mysql.executeRequeteSystem(param.getRequete());
				return null;
			}
		}
		throw new ApplicationException(400, "Container inconnu");
	}

}
