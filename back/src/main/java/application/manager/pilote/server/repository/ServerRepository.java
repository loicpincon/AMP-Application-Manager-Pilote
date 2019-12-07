package application.manager.pilote.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import application.manager.pilote.server.modele.Server;

@Repository
public interface ServerRepository extends MongoRepository<Server, Integer> {

}
