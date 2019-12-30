package application.manager.pilote.docker.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.docker.mapper.ContainerMapper;
import application.manager.pilote.docker.modele.Container;

@Service
public class DockerContainerService {

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ContainerMapper containerMapper;

	/**
	 * 
	 * @param dockerFile
	 */
	public void createContainer(String dockerFile, String nameContainer) {
		this.createContainer(new File(dockerFile), nameContainer);
	}

	/**
	 * 
	 * @param dockerFile
	 */
	public void createContainer(File dockerFile, String nameContainer) {
		try {
			BuildImageResultCallback callback = new BuildImageResultCallback() {
				@Override
				public void onNext(BuildResponseItem item) {
					super.onNext(item);
				}
			};
			String test = dockerClient.buildImageCmd(dockerFile).exec(callback).awaitCompletion().awaitImageId();
			Ports portBindings = new Ports();
			ExposedPort expoPort = new ExposedPort(80);
			portBindings.bind(expoPort, Binding.bindPort(1111));
			CreateContainerResponse container = dockerClient.createContainerCmd(test).withPublishAllPorts(true)
					.withName(nameContainer).withPortBindings(portBindings).exec();
			dockerClient.startContainerCmd(container.getId()).exec();

		} catch (DockerException | InterruptedException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public List<Container> getContainers() {
		List<Container> retour = new ArrayList<>();
		for (com.github.dockerjava.api.model.Container container : dockerClient.listContainersCmd().exec()) {
			retour.add(this.containerMapper.mapFrom(container));
		}
		return retour;
	}

}
