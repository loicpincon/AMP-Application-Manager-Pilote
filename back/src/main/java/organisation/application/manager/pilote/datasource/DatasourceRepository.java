package organisation.application.manager.pilote.datasource;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasourceRepository extends MongoRepository<DataSource, String> {

}
