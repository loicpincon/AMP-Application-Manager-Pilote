package organisation.application.manager.pilote.datasource.mongo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PortBinding;

import organisation.application.manager.pilote.commun.helper.PropertiesReader;
import organisation.application.manager.pilote.commun.helper.RandomPortHelper;
import organisation.application.manager.pilote.commun.service.DefaultService;
import organisation.application.manager.pilote.datasource.commun.modele.DataSource;
import organisation.application.manager.pilote.datasource.commun.modele.DataSourceEnum;
import organisation.application.manager.pilote.datasource.commun.service.pr.DataSourcePR;
import organisation.application.manager.pilote.datasource.mongo.modele.MongoDataSource;
import organisation.application.manager.pilote.datasource.mongo.repository.MongoDataSourceRepository;

@Service
public class MongoBaseService extends DefaultService {

	@Autowired
	private MongoDataSourceRepository repo;

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private PropertiesReader properties;
	@Autowired
	private RandomPortHelper randomPort;

	public DataSource inserer(DataSourcePR param) {
		MongoDataSource mongo = new MongoDataSource();
		mongo.setIdApp(param.getIdApp());
		mongo.setPort(randomPort.randomPort().toString());
		mongo.setIp(properties.getPropertyOrElse("IP_SERVER_DOCKER", "localhost"));
		mongo.setUser(param.getUser());
		mongo.setPassword(param.getPassword());
		mongo.setType(DataSourceEnum.MONGO.name());
//		dockerClient.pullImageCmd("mongo").withTag("latest").exec(new PullImageResultCallback() {
//			@Override
//			public void onNext(PullResponseItem item) {
//				super.onNext(item);
//			}
//		}).awaitSuccess();
		CreateContainerResponse container = dockerClient.createContainerCmd("mongo:latest")
				.withPortBindings(PortBinding.parse(mongo.getPort() + ":27017")).exec();
		mongo.setContainerId(container.getId());
		dockerClient.startContainerCmd(mongo.getContainerId()).exec();
		return repo.insert(mongo);
	}

	public List<MongoDataSource> recupererServeur(String idApp) {
		if (idApp != null) {
			return repo.findAll().stream().filter(dataS -> idApp.equals(dataS.getIdApp())).collect(Collectors.toList());
		} else {
			return repo.findAll();
		}
	}

}
