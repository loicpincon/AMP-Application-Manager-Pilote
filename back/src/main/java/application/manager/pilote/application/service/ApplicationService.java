package application.manager.pilote.application.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.repository.ApplicationRepository;
import application.manager.pilote.commun.service.DefaultCrudService;

@Service
public class ApplicationService implements DefaultCrudService<Application, String> {

	@Autowired
	private ApplicationRepository appRepo;

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
		param.setId(Hashing.sha256().hashString(new Date() + param.getName(), UTF_8).toString());
		return appRepo.insert(param);
	}

	@Override
	public Application modifier(Application param) {
		return appRepo.save(param);
	}

}
