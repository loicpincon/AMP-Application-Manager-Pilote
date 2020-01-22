package application.manager.pilote.docker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.docker.modele.DockerFile;
import application.manager.pilote.docker.repository.DockerFileRepository;
import application.manager.pilote.docker.service.pr.DockerFileParam;

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

	public DockerFile insert(DockerFileParam param) {
		DockerFile dockerFile = new DockerFile();
		dockerFile.setId(hasherService.randomInt());
		dockerFile.setName(param.getName());
		dockerFile.setFile(param.getFile());
		dockerFile.setIsPublic(param.getIsPublic());
		dockerFileRepo.insert(dockerFile);
		return dockerFile;
	}

	public DockerFile modifier(DockerFile param) {
		return dockerFileRepo.save(param);
	}

	public Object supprimer(Integer id) {
		dockerFileRepo.deleteById(id);
		return null;
	}

}
