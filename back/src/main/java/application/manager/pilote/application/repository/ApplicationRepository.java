package application.manager.pilote.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import application.manager.pilote.application.modele.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, Integer> {

}
