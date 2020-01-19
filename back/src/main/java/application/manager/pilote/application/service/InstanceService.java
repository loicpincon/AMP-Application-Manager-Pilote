package application.manager.pilote.application.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.Livrable;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.repository.InstanceRepository;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.helper.RandomPortHelper;
import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class InstanceService {

	protected static final Log LOG = LogFactory.getLog(InstanceService.class);

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

	@Autowired
	private RandomPortHelper randomPortHelper;

	@Autowired
	private StringHelper stringUtils;

	/**
	 * Ajouter une instance a une application existante
	 * 
	 * @param id
	 * @param serveur
	 * @param instance
	 * @return
	 */
	public Instance ajouter(String id, Integer serveur) {
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
		Instance instance = new Instance();
		instance.setEtat("V");

		instance.setLibelle(server.getNom() + "-" + (env.getInstances().size() + 1));
		instance.setPort(randomPortHelper.randomPort().toString());
		instance.setId(hasher.hash(id + server.getId() + new Date()));
		env.getInstances().add(instance);
		instance.setContainerId(instance.getId());
		appService.modifier(app);
		return instance;
	}

	/**
	 * @param id
	 * @return
	 */
	public Instance consulter(List<Instance> instances, String id) {
		for (Instance i : instances) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		throw new ApplicationException(HttpStatus.NOT_FOUND, "Instance non trouve");
	}

	/**
	 * @param instance
	 * @return
	 */
	public Instance modifier(Instance instance) {
		return instanceRepo.save(instance);
	}

	/**
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
		livrable.setId(hasher.randomInt().toString());
		livrable.setNom(version);
		livrable.setPathtoFile("");
		app.getLivrables().add(livrable);
		String path = idApp + "/" + version;
		livrable.setPathtoFile(path + "/" + app.getBaseName());
		appService.modifier(app);
		writeFileToPath(file, path, app.getBaseName());
		return livrable;
	}

	/**
	 * @param multipart
	 * @param fileName
	 * @throws IOException
	 */
	private void writeFileToPath(MultipartFile multipart, String path, String fileName) throws IOException {
		String pathWithoutNameFile = properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + "/" + path;

		createDossierRecursif(pathWithoutNameFile);
		Files.copy(multipart.getInputStream(), Paths.get(stringUtils.concat(pathWithoutNameFile, "/", fileName)));
		new File(stringUtils.concat(pathWithoutNameFile, "/", fileName));

	}

	/**
	 * 
	 * @param path
	 */
	private void createDossierRecursif(String path) {
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
	}

}
