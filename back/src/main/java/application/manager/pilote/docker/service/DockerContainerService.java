package application.manager.pilote.docker.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.commun.service.HashService;
import application.manager.pilote.docker.helper.DeployFileHelper;
import application.manager.pilote.docker.mapper.ContainerMapper;
import application.manager.pilote.docker.modele.Container;
import application.manager.pilote.docker.service.pr.ContainerParam;

@Service
public class DockerContainerService {

	private static final String SLASH = "/";

	protected static final Log LOG = LogFactory.getLog(DockerContainerService.class);

	private static final String BASE_PATH_TO_DOCKERFILE_DEFAULT = "/var/base/dockerfile";

	private static final String BASE_PATH_TO_DOCKERFILE = "BASE_PATH_TO_DOCKERFILE";

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ContainerMapper containerMapper;

	@Autowired
	private DockerFileService dockerFileService;

	@Autowired
	private DeployFileHelper deployfileHelper;

	@Autowired
	private PropertiesReader properties;

	@Autowired
	private HashService hasherService;

	@Autowired
	private StringHelper stringUtils;

	/**
	 * 
	 * @param dockerFile
	 */
	public Container createContainer(ContainerParam param) {

		String pathToFolderTemporaire = stringUtils.concat(
				properties.getPropertyOrElse(BASE_PATH_TO_DOCKERFILE, BASE_PATH_TO_DOCKERFILE_DEFAULT), SLASH,
				hasherService.randomInt());
		if (this.dockerClient.inspectContainerCmd(param.getContainerName()).exec() != null) {
			this.delete(param.getContainerName());
		}

		try {
			BuildImageResultCallback callback = new BuildImageResultCallback() {
				@Override
				public void onNext(BuildResponseItem item) {
					super.onNext(item);
				}
			};

			File dockerFile = deployfileHelper.createDockerFile(pathToFolderTemporaire,
					dockerFileService.get(param.getDockerFileId()));

			String test = dockerClient.buildImageCmd(dockerFile).withBuildArg("-t", "ceiciestie").exec(callback)
					.awaitCompletion().awaitImageId();
			LOG.info(test);
			CreateContainerResponse container = dockerClient.createContainerCmd(test).withPublishAllPorts(true)
					.withName(param.getContainerName()).withPortBindings(getPortsBinds()).exec();

			dockerClient.startContainerCmd(container.getId()).exec();

			return new Container();
		} catch (DockerException | InterruptedException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private Ports getPortsBinds() {
		Ports portBindings = new Ports();

		ExposedPort expoPort = new ExposedPort(8080);
		portBindings.bind(expoPort, Binding.bindPort(7894));
		return portBindings;
	}

	public List<Container> getContainers() {
		List<Container> retour = new ArrayList<>();
		for (com.github.dockerjava.api.model.Container container : dockerClient.listContainersCmd().exec()) {
			retour.add(this.containerMapper.mapFrom(container));
		}
		return retour;
	}

	private void start(String containerId) {
		try {
			dockerClient.startContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	public Container manage(String id, String action) {
		switch (action) {
		case "start":
			this.start(id);
			break;
		case "reload":
			this.reload(id);
			break;
		case "stop":
			this.stop(id);
			break;
		case "delete":
			this.delete(id);
			break;
		default:
			throw new ApplicationException(400, "action inconnue");
		}
		return new Container();
	}

	private void stop(String containerId) {
		try {
			dockerClient.stopContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	private void reload(String containerId) {
		try {
			dockerClient.restartContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

	public void delete(String containerId) {
		try {
			dockerClient.stopContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
		String status = dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus();
		if (status != null && status.equals("running")) {
			this.stop(containerId);
		}
		dockerClient.removeContainerCmd(containerId).exec();
	}

	public InspectContainerResponse inspect(String containerId) {
		try {
			return dockerClient.inspectContainerCmd(containerId).exec();
		} catch (DockerException e) {
			throw new ApplicationException(400, e.getMessage());
		}
	}

}
