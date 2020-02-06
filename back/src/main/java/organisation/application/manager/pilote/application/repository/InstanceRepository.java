package organisation.application.manager.pilote.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import organisation.application.manager.pilote.application.modele.Instance;

@Repository
public interface InstanceRepository extends MongoRepository<Instance, String> {

}
