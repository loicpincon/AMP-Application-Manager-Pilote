package organisation.application.manager.pilote.datasource.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import organisation.application.manager.pilote.datasource.mongo.modele.MongoDataSource;

@Repository
public interface MongoDataSourceRepository extends MongoRepository<MongoDataSource, String> {

}
