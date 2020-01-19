package application.manager.pilote.application.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import application.manager.pilote.application.modele.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

	List<Application> findByIdIn(List<String> id);

	
}
