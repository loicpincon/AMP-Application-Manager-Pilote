package organisation.application.manager.pilote.datasource.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PortBinding;

import organisation.application.manager.pilote.application.modele.Application;
import organisation.application.manager.pilote.application.service.ApplicationService;
import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.helper.RandomPortHelper;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.helper.MySqlConnector;
import organisation.application.manager.pilote.datasource.modele.DataSource;
import organisation.application.manager.pilote.datasource.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.modele.DatasourceRepository;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.service.pr.RequeteParam;
import organisation.application.manager.pilote.server.modele.Server;
import organisation.application.manager.pilote.server.service.ServerService;

@Service
public class MySqlService extends DefaultService {

	protected static final Log LOG = LogFactory.getLog(MySqlService.class);

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private DatasourceRepository datasourceRepo;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ServerService serService;

	@Autowired
	private RandomPortHelper randomPort;

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
			MysqlDataSource datasource = (MysqlDataSource) datasourceO.get();
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

	public DataSource ajouterDatasource(String idApp) {
		DataSource datasource = new DataSource();
		datasource.setIdApp(idApp);
		datasource.setPort(randomPort.randomPort().toString());
		datasource.setIp("localhost");
		datasource.setUser("root");
		try {
			InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		datasource.setPassword("password");
		datasource.setType(DataSourceEnum.MYSQL.name());
		CreateContainerResponse container = dockerClient.createContainerCmd("mysql:latest")
				.withEnv("MYSQL_ROOT_PASSWORD=" + datasource.getPassword()).withName("mysql-" + idApp)
				.withPortBindings(PortBinding.parse(datasource.getPort() + ":3306")).exec();
		datasource.setContainerId(container.getId());
		dockerClient.startContainerCmd(datasource.getContainerId()).exec();
		datasourceRepo.save(datasource);
		return datasource;
	}

	public Object getTablesOfBases(String idContainer, String idbase) {
		Optional<DataSource> datasourceO = datasourceRepo.findById(idContainer);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();
			MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(), datasource.getUser(),
					datasource.getPassword(), idbase);
			return mysql.execute("show tables");
		}
		throw new ApplicationException(400, "Container inconnu");
	}

}
