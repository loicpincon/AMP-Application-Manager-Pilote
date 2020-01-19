package application.manager.pilote.utilisateur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import application.manager.pilote.utilisateur.modele.Utilisateur;

@Repository
public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {

	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	@Query("{'login' : ?0 , 'password' : ?1}")
	Optional<Utilisateur> trouverParLoginEtMotDePasse(String login, String password);

	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	@Query("{'token' : ?0}")
	Optional<Utilisateur> findByToken(String token);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query(value = "{'rights.applicationId' : ?0 }")
	List<Utilisateur> findUserByApplication(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query("{'$or' : [{'nom' : {$regex :?0}}, {'prenom' : {$regex :?0}},{'login' : {$regex :?0}}]}")
	List<Utilisateur> findByNomOrPrenomOrLogin(String id);

}
