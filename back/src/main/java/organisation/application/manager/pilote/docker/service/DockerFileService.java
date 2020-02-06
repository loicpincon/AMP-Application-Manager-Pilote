package organisation.application.manager.pilote.docker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.commun.service.HashService;
import organisation.application.manager.pilote.docker.modele.DockerFile;
import organisation.application.manager.pilote.docker.repository.DockerFileRepository;

@Service
public class DockerFileService {

	@Autowired
	private DockerFileRepository dockerFileRepo;

	@Autowired
	private HashService hasherService;

	public List<DockerFile> getAll() {
		return dockerFileRepo.findAll();
	}

	public DockerFile get(Integer id) {
		Optional<DockerFile> dockerFile = dockerFileRepo.findById(id);
		if (!dockerFile.isPresent()) {
			throw new ApplicationException(400, "Dockerfile id is incorrect");
		}
		return dockerFile.get();
	}

	public DockerFile insert(DockerFile dockerFile) {
		dockerFile.setId(hasherService.randomInt());
		return dockerFileRepo.insert(dockerFile);
	}

	public DockerFile modifier(DockerFile param) {
		return dockerFileRepo.save(param);
	}

	public Object supprimer(Integer id) {
		dockerFileRepo.deleteById(id);
		return null;
	}

}
