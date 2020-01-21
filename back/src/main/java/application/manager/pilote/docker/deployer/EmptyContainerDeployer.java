package application.manager.pilote.docker.deployer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.docker.helper.ScriptPathHelper;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

@Builder
public class EmptyContainerDeployer extends DefaultDeployer {

	protected static final Log LOG = LogFactory.getLog(EmptyContainerDeployer.class);

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ScriptPathHelper scriptPathHelper;

	Application app;

	Server server;

	private Instance instance;

	@Override
	public void run() {
		BuildImageResultCallback callback = new BuildImageResultCallback();
		File dockerFile = new File(scriptPathHelper.getRelativePath("/Dockerfile/EmptyContainer/Dockerfile"));

		String test;
		try {
			test = dockerClient.buildImageCmd(dockerFile).exec(callback).awaitCompletion().awaitImageId();

			Map<String, String> labels = new HashMap<>();
			labels.put("ID_APP", app.getId());
			labels.put("ID_ENV", server.getId().toString());
			labels.put("PORT_HTTP_EXT", instance.getPort());

			dockerClient.createContainerCmd(test).withName(instance.getId()).withPortBindings(getPortsBinds(instance))
					.withPublishAllPorts(true).withLabels(labels).exec();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// LOG.info(test);
		LOG.debug("Fin de l'ajout du nouveau container");

	}

}
