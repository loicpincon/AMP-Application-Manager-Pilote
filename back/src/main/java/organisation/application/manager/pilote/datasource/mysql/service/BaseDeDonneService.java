package organisation.application.manager.pilote.datasource.mysql.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.modele.DataSourceItem;
import organisation.application.manager.pilote.datasource.mysql.mapper.BaseDeDonneeRessourceMapper;
import organisation.application.manager.pilote.datasource.mysql.modele.BaseDeDonneeRessource;
import organisation.application.manager.pilote.datasource.mysql.modele.DefaultMysqlDecoreDataBean;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;
import organisation.application.manager.pilote.datasource.mysql.modele.MysqlTable;
import organisation.application.manager.pilote.datasource.mysql.modele.ServeurParam;
import organisation.application.manager.pilote.datasource.mysql.repository.MySqlDataSourceRepository;

@Service
public class BaseDeDonneService extends DefaultService {

	private static final String REQ_DETAILS_BASE = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'membre_DEV';";

	@Autowired
	private MysqlRequeteService mysql;

	@Autowired
	private MySqlDataSourceRepository mySqlDataSourceRepository;

	@Autowired
	private BaseDeDonneeRessourceMapper baseDeDonneeRessourceMapper;

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
				.findFirst().orElseThrow();
		sp.setDatasource(item.getName());
		sp.setLogin(item.getUser());
		sp.setPassword(item.getPassword());
		BaseDeDonneeRessource retour = baseDeDonneeRessourceMapper.map(mysqlDataSource, item);

		retour.setTables(consulter(sp));

		return retour;
	}

	public BaseDeDonneeRessource inserer(BaseDeDonneeRessourcePR param) {
		MysqlDataSource mysql = new MysqlDataSource();
		mysql.setIdApp(param.getIdApp());
		mysql.setIp(param.getIp());
		mysql.setPassword(param.getPassword());
		mysql.setPort(param.getPort());
		mysql.setType(param.getType());
		mysql.setUser(param.getUser());
		mysql.setBases(new ArrayList<DataSourceItem>());
		mySqlDataSourceRepository.insert(mysql);
		return baseDeDonneeRessourceMapper.map(mysql, null);

	}

	public List<MysqlTable> consulter(ServeurParam conf) {
		return new DefaultMysqlDecoreDataBean<MysqlTable>(mysql.select(REQ_DETAILS_BASE, conf), MysqlTable.class)
				.getAll();
	}

	public DataSourceItem insererBase(String id, DataSourceItem body) {
		MysqlDataSource mysql = trouverParId(id);
		mysql.getBases().add(body);
		mySqlDataSourceRepository.save(mysql);
		return body;
	}

	public List<MysqlDataSource> recupererServeur() {
		return mySqlDataSourceRepository.findAll();
	}

	public List<DataSourceItem> recupererBase(String id) {
		return trouverParId(id).getBases();
	}

}
