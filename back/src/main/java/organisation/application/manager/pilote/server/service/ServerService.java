package organisation.application.manager.pilote.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.server.modele.Server;
import organisation.application.manager.pilote.server.repository.ServerRepository;

@Service
public class ServerService {

	@Autowired
	private ServerRepository serverRepo;

	/**
	 * 
	 * @param server
	 * @return
	 */
	public Server inserer(Server server) {
		return serverRepo.insert(server);
	}

	/**
	 * 
	 * @return
	 */
	public List<Server> recuperer() {
		return serverRepo.findAll();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Server consulter(Integer id) {
		return serverRepo.findById(id).get();
	}

}
