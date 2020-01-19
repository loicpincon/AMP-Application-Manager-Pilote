package application.manager.pilote.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.repository.ServerRepository;import application.manager.pilote.session.service.SessionService;

@Service
public class ServerService {

	@Autowired
	private ServerRepository serverRepo;
	
	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private SessionService sessionService;

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

	public List<Server> recupererParUser() {
		List<Server> server = new ArrayList<Server>();
		List<Application> apps = appService.recupererParUser(sessionService.getSession().getToken());
		for(Application app:apps) {
			
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
