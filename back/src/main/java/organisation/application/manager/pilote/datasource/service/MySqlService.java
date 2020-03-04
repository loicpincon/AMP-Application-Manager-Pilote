package organisation.application.manager.pilote.datasource.service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.service.ApplicationService;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.helper.MySqlConnector;
import organisation.application.manager.pilote.datasource.modele.DataSource;
import organisation.application.manager.pilote.datasource.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.modele.DatasourceRepository;
import organisation.application.manager.pilote.datasource.service.pr.RequeteParam;
import organisation.application.manager.pilote.server.modele.Server;
import organisation.application.manager.pilote.server.service.ServerService;

@Service
public class MySqlService extends DefaultService {

	protected static final Log LOG = LogFactory.getLog(MySqlService.class);

	@Autowired
	private DatasourceRepository datasourceRepo;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ServerService serService;
	
	
	public Object exporterBase(String containerId, RequeteParam param) {
		Optional<DataSource> datasourceO = datasourceRepo.findById(containerId);
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
				return null;
			}
		}
		throw new ApplicationException(400, "Container inconnu");
	}

	public Object inserer(String idApp, String containerId, String name) {

		// INSERER DANS BASE

		Optional<DataSource> datasourceO = datasourceRepo.findById(containerId);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();

			Application app = appService.consulter(idApp);

			Set<Integer> cles = app.getEnvironnements().keySet();
			Iterator<Integer> it = cles.iterator();
			while (it.hasNext()) {
				Integer cle = it.next(); // tu peux typer plus finement ici
				Server server = serService.consulter(cle);

				DataSourceItem item = new DataSourceItem();
				item.setName(name + "_" + server.getNom());
				item.setUser("user-" + item.getName());
				item.setPassword("password-" + item.getName());
				datasource.getBases().add(item);
				MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(),
						datasource.getUser(), datasource.getPassword(), "mysql");
				mysql.executeRequeteSystem("CREATE DATABASE " + name + "_" + server.getNom() + " ;");
				mysql.executeRequeteSystem(
						"CREATE USER '" + item.getUser() + "'@'%' IDENTIFIED BY '" + item.getPassword() + "'");
				mysql.executeRequeteSystem(
						"GRANT ALL PRIVILEGES ON " + item.getName() + ".* TO '" + item.getUser() + "'@'%';");
			}
			datasourceRepo.save(datasource);

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

	public Object recuperer(String idApp) {
		return datasourceRepo.findByIdApp(idApp);
	}

}
