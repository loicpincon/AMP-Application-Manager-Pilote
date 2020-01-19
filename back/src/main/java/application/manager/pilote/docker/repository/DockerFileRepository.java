package application.manager.pilote.docker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import application.manager.pilote.docker.modele.DockerFile;

@Repository
public interface DockerFileRepository extends MongoRepository<DockerFile, Integer> {

}
