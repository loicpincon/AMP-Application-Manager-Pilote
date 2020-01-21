package application.manager.pilote.docker.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse.ContainerState;

import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class ApplicationPodsService {
	protected static final Log LOG = LogFactory.getLog(ApplicationPodsService.class);

	private static final String ID_ENV = "ID_ENV";
	private static final String ID_APP = "ID_APP";

	@Autowired
	DockerContainerService dockerService;

	@Autowired
	ServerService serverSevice;

	public List<Instance> recupererInstanceParAppEtEnv(String idApp, Integer idEnv) {
		List<Instance> instances = new ArrayList<>();
		Server server = serverSevice.consulter(idEnv);
		List<com.github.dockerjava.api.model.Container> containers = dockerService.getContainers();
		for (com.github.dockerjava.api.model.Container container : containers) {
			LOG.debug(container.getId());
			if (checkLabelPresence(container.getLabels().get(ID_APP), idApp)
					&& checkLabelPresence(container.getLabels().get(ID_ENV), idEnv.toString())) {
				instances.add(mapFrom(container, server));
			}
		}
		return instances;
	}

	private Boolean checkLabelPresence(String label, String cond) {
		LOG.debug(label + " : " + cond);
		return label != null && label.equals(cond);
	}

	private Instance mapFrom(com.github.dockerjava.api.model.Container container, Server server) {
		InspectContainerResponse containerInspect = dockerService.inspect(container.getId());
		Instance i = new Instance();
		i.setContainerId(container.getId());
		i.setEtat(setEtatToContainer(containerInspect.getState()));
		i.setId(container.getId());
		i.setLibelle(container.getStatus());
		i.setPort(container.getPorts()[0].getPublicPort().toString());
		i.setUrl("http://" + server.getIp() + ":" + i.getPort());
		i.setVersionApplicationActuel(container.getLabels().get("VERSION_APP"));
		i.setVersionParametresActuel(container.getLabels().get("VERSION_PARAM"));
		return i;
	}

	private String setEtatToContainer(ContainerState etat) {
		if (etat.getRunning()) {
			return "L";
		} else if (etat.getPaused()) {
			return "S";
		} else {
			return "V";
		}
	}
}
