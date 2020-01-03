package application.manager.pilote.application.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.repository.ApplicationRepository;
import application.manager.pilote.commun.service.DefaultCrudService;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.session.service.SessionService;

@Service
public class ApplicationService implements DefaultCrudService<Application, String> {

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private HashService hashService;

	@Autowired
	private SessionService sesionContext;

	@Override
	public Application consulter(String id) {
		return appRepo.findById(id).get();
	}

	@Override
	public List<Application> recuperer() {
		return appRepo.findAll();
	}

	@Override
	public Application inserer(Application param) {
		param.setId(hashService.hash(new Date() + param.getName()));
		return appRepo.insert(param);
	}

	@Override
	public Application modifier(Application param) {
		return appRepo.save(param);
	}

}
