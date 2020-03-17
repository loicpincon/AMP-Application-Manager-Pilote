package organisation.application.manager.pilote.datasource.mysql.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import organisation.application.manager.pilote.datasource.mysql.modele.MysqlDataSource;

@Repository
public interface MySqlDataSourceRepository extends MongoRepository<MysqlDataSource, String> {

}
