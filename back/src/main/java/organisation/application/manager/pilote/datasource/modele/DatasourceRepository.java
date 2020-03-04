package organisation.application.manager.pilote.datasource.modele;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasourceRepository extends MongoRepository<DataSource, String> {

	List<DataSource> findByIdApp(String idApp);

}
