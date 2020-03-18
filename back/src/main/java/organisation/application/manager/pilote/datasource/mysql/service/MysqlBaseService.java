package organisation.application.manager.pilote.datasource.mysql.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PortBinding;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.helper.PropertiesReader;
import organisation.application.manager.pilote.commun.helper.RandomPortHelper;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.commun.service.pr.DataSourcePR;
import organisation.application.manager.pilote.datasource.mysql.helper.MySqlConnector;
import organisation.application.manager.pilote.datasource.mysql.mapper.BaseDeDonneeRessourceMapper;
import organisation.application.manager.pilote.datasource.mysql.modele.BaseDeDonneeRessource;
import organisation.application.manager.pilote.datasource.mysql.modele.DefaultMysqlDecoreDataBean;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlTable;
import organisation.application.manager.pilote.datasource.mysql.modele.ServeurParam;
import organisation.application.manager.pilote.datasource.mysql.repository.MySqlDataSourceRepository;

@Service
public class MysqlBaseService extends DefaultService {

	private static final String REQ_DETAILS_BASE = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'membre_DEV';";

	@Autowired
	private MysqlRequeteService mysql;

	@Autowired
	private MySqlDataSourceRepository mySqlDataSourceRepository;

	

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private BaseDeDonneeRessourceMapper baseDeDonneeRessourceMapper;

	@Autowired
	private PropertiesReader properties;
	@Autowired
	private RandomPortHelper randomPort;

	private MysqlDataSource trouverParId(String id) {
		Optional<MysqlDataSource> mysqlDateSourceO = mySqlDataSourceRepository.findById(id);
		if (!mysqlDateSourceO.isPresent()) {
			throw new ApplicationException(401, "Datasource introuvable  : " + id);
		}
		return mysqlDateSourceO.get();
	}

	public BaseDeDonneeRessource consulter(String id, String name) {
		MysqlDataSource mysqlDataSource = trouverParId(id);

		ServeurParam sp = new ServeurParam();
		sp.setPort(mysqlDataSource.getPort());
		sp.setHost(mysqlDataSource.getIp());

		DataSourceItem item = mysqlDataSource.getBases().stream().filter(itemTmp -> name.equals(itemTmp.getName()))
				.findFirst().get();
		if (item == null) {
			throw new ApplicationException(400, "Probleme");
		}
		sp.setDatasource(item.getName());
		sp.setLogin(item.getUser());
		sp.setPassword(item.getPassword());
		BaseDeDonneeRessource retour = baseDeDonneeRessourceMapper.map(mysqlDataSource, item);

		retour.setTables(consulter(sp));

		return retour;
	}

	public MysqlDataSource inserer(DataSourcePR param) {
		MysqlDataSource datasource = new MysqlDataSource();
		datasource.setIdApp(param.getIdApp());
		datasource.setPort(randomPort.randomPort().toString());
		datasource.setIp(properties.getPropertyOrElse("IP_SERVER_DOCKER", "localhost"));
		datasource.setUser(param.getUser());
		datasource.setPassword(param.getPassword());
		datasource.setType(DataSourceEnum.MYSQL.name());
		CreateContainerResponse container = dockerClient.createContainerCmd("mysql:latest")
				.withEnv("MYSQL_ROOT_PASSWORD=" + datasource.getPassword()).withName("mysql-" + param.getIdApp())
				.withPortBindings(PortBinding.parse(datasource.getPort() + ":3306")).exec();
		datasource.setContainerId(container.getId());
		dockerClient.startContainerCmd(datasource.getContainerId()).exec();
		mySqlDataSourceRepository.insert(datasource);
		return datasource;

	}

	public List<MysqlTable> consulter(ServeurParam conf) {
		return new DefaultMysqlDecoreDataBean<MysqlTable>(mysql.select(REQ_DETAILS_BASE, conf), MysqlTable.class)
				.getAll();
	}

	public DataSourceItem insererBase(String id, DataSourceItem body) {
		MysqlDataSource mysql = trouverParId(id);
		body.setUser(body.getName());
		body.setPassword("root");
		mysql.getBases().add(body);
		MySqlConnector mysqlC = new MySqlConnector(mysql.getIp(), mysql.getPort(), mysql.getUser(), mysql.getPassword(),
				"mysql");
		mysqlC.executeRequeteSystem("CREATE DATABASE " + body.getName() + ";");
		mysqlC.executeRequeteSystem(
				"CREATE USER '" + body.getUser() + "'@'%' IDENTIFIED BY '" + body.getPassword() + "'");
		mysqlC.executeRequeteSystem(
				"GRANT ALL PRIVILEGES ON " + body.getName() + ".* TO '" + body.getUser() + "'@'%';");
		mySqlDataSourceRepository.save(mysql);
		return body;
	}

	/**
	 * 
	 * @param idApp
	 * @return
	 */
	public List<MysqlDataSource> recupererServeur(String idApp) {
		if (idApp != null) {
			return mySqlDataSourceRepository.findAll().stream().filter(dataS -> idApp.equals(dataS.getIdApp()))
					.collect(Collectors.toList());
		} else {
			return mySqlDataSourceRepository.findAll();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<DataSourceItem> recupererBase(String id) {
		return trouverParId(id).getBases();
	}

	public Object getTablesOfBases(String idContainer, String idbase) {
		Optional<MysqlDataSource> datasourceO = mySqlDataSourceRepository.findById(idContainer);
		if (datasourceO.isPresent()) {
			DataSource datasource = datasourceO.get();
			MySqlConnector mysql = new MySqlConnector(datasource.getIp(), datasource.getPort(), datasource.getUser(),
					datasource.getPassword(), idbase);
			return mysql.execute("show tables");
		}
		throw new ApplicationException(400, "Container inconnu");
	}

}
