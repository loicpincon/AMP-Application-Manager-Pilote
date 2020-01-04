package application.manager.pilote.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.repository.InstanceRepository;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class InstanceService {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ServerService servService;

	@Autowired
	private HashService hasher;

	@Autowired
	private InstanceRepository instanceRepo;

	/**
	 * Ajouter une instance a une application existante
	 * 
	 * @param id
	 * @param serveur
	 * @param instance
	 * @return
	 */
	public Instance ajouter(String id, Integer serveur, Instance instance) {
		Application app = appService.consulter(id);
		Server server = servService.consulter(serveur);
		if (app.getEnvironnements() == null) {
			app.setEnvironnements(new HashMap<>());
		}
		Environnement env = app.getEnvironnements().get(server.getId());
		if (env == null) {
			env = new Environnement();
			env.setInstances(new ArrayList<>());
			env.setParametres(new ArrayList<>());
			app.getEnvironnements().put(server.getId(), env);

		}
		instance.setId(hasher.hash(id + server.getId() + new Date()));
		env.getInstances().add(instance);
		appService.modifier(app);
		return instance;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Instance consulter(String id) {
		Optional<Instance> insOpt = instanceRepo.findById(id);
		if (!insOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "Instance non trouve");
		}
		return insOpt.get();
	}

	public Instance modifier(Instance instance) {
		return instanceRepo.save(instance);
	}

}
