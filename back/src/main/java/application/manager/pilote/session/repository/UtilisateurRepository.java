package application.manager.pilote.session.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import application.manager.pilote.session.modele.Utilisateur;

@Repository
public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {

	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	@Query("{ 'login' : ?0},{ 'password' : ?1}")
	Optional<Utilisateur> trouverParLoginEtMotDePasse(String login, String password);

}
