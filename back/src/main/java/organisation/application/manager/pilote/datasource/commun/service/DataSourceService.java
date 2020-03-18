package organisation.application.manager.pilote.datasource.commun.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.commun.service.pr.DataSourcePR;
import organisation.application.manager.pilote.datasource.mongo.service.MongoBaseService;
import organisation.application.manager.pilote.datasource.mysql.service.MysqlBaseService;

@Service
public class DataSourceService {

	@Autowired
	private MysqlBaseService mysqlBaseService;

	@Autowired
	private MongoBaseService mongoBaseService;

	public DataSource ajouter(DataSourcePR param) {
		DataSourceEnum type = DataSourceEnum.valueOf(param.getType());
		if (type.equals(DataSourceEnum.MYSQL)) {
			return mysqlBaseService.inserer(param);
		} else if (type.equals(DataSourceEnum.MONGO)) {
			return mongoBaseService.inserer(param);
		} else {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, "Le type de datasource est inconnu");
		}
	}

	/**
	 * 
	 * @param idApp
	 * @return
	 */
	public List<DataSource> recuperer(String idApp) {
		List<DataSource> datas = new ArrayList<>();
		datas.addAll(mysqlBaseService.recupererServeur(idApp));
		datas.addAll(mongoBaseService.recupererServeur(idApp));
		return datas;
	}
}
