package application.manager.pilote.application.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.Livrable;
import application.manager.pilote.application.repository.InstanceRepository;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class InstanceService {

	private static final String BASE_PATH_TO_APPLICATION_STOCK = "BASE_PATH_TO_APPLICATION_STOCK";

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ServerService servService;

	@Autowired
	private HashService hasher;

	@Autowired
	private InstanceRepository instanceRepo;

	@Autowired
	private PropertiesReader properties;

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

	/**
	 * 
	 * @param instance
	 * @return
	 */
	public Instance modifier(Instance instance) {
		return instanceRepo.save(instance);
	}

	/**
	 * 
	 * @param file
	 * @param idApp
	 * @param version
	 * @return
	 * @throws IOException
	 */
	public Livrable uploadFileVersion(MultipartFile file, String idApp, String version) throws IOException {
		Livrable livrable = new Livrable();
		Application app = appService.consulter(idApp);
		livrable.setDateUpload(new Date());
		livrable.setFolder(false);
		livrable.setId(new Date().toString());
		livrable.setNom(file.getName());
		livrable.setPathtoFile("");
		app.getLivrables().add(livrable);
		appService.modifier(app);
		writeFileToPath(file, idApp + "/" + version + "/" + file.getOriginalFilename());
		return livrable;
	}

	/**
	 * 
	 * @param multipart
	 * @param fileName
	 * @throws IOException
	 */
	private void writeFileToPath(MultipartFile multipart, String fileName) throws IOException {
		File convFile = new File(properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + "/" + fileName);
		convFile.mkdirs();
		multipart.transferTo(convFile);
	}

}
