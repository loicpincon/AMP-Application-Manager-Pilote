package application.manager.pilote.application.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.repository.ApplicationRepository;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.service.DefaultCrudService;
import application.manager.pilote.commun.service.HashService;

@Service
public class ApplicationService implements DefaultCrudService<Application, String> {

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private HashService hashService;

	@Override
	public Application consulter(String id) {
		Optional<Application> appOpt = appRepo.findById(id);
		if (!appOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.NOT_FOUND, "application non trouve");
		}
		return appOpt.get();
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
