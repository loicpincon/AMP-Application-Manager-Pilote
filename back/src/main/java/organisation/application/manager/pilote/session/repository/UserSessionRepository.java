package organisation.application.manager.pilote.session.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import organisation.application.manager.pilote.session.modele.UserSession;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSession, String> {

}
