package application.manager.pilote.docker.deployer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;

import application.manager.pilote.application.modele.AngularApplication;
import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

public class AngularAppDeployer extends DefaultDeployer<AngularApplication> {

	protected static final Log LOG = LogFactory.getLog(AngularAppDeployer.class);

	Server server;

	@Builder
	public AngularAppDeployer(Application app, Instance ins, Server server) {
		super((AngularApplication) app, ins);
		this.server = server;
	}

	@Override
	public void run() {
		LOG.debug("Debut du deploiement de angular application");
		if (!app.getIsBuilder()) {
			LOG.debug("Telechargement et Build de l'application --prod en cours");
			buildProcessus(genererCheminTemporaire(), app.getBaseLocation(), app.getBaseName());
			LOG.debug("Fin Telechargement et Build de l'application --prod ");
		}
		Map<String, String> params = new HashMap<>();
		params.put("dossier", "dist/" + app.getBaseName());
		createContainer(params);
		ins.setEtat("L");
		if (server.getDns() != null) {
			ins.setUrl("http://" + server.getDns() + ":" + ins.getPort());
		} else {
			ins.setUrl("http://" + server.getIp() + ":" + ins.getPort());
		}
		ins.setVersionApplicationActuel("INDISPONIBLE");
		ins.setVersionParametresActuel("INDISPONIBLE");
		ins.getUserActions().add(traceAction("Deploy", "Succes", "bientot disponible"));
		template.convertAndSend("/content/application", ins);
		LOG.debug("fin du thread");
		appService.modifier(app);
	}

	@Override
	protected Ports getPortsBinds(Instance ins) {
		Ports portBindings = new Ports();
		ExposedPort expoPort = new ExposedPort(80);
		portBindings.bind(expoPort, Binding.bindPort(Integer.valueOf(ins.getPort())));
		return portBindings;
	}

	private void buildProcessus(String path, String base, String folderName) {
		try {
			ProcessBuilder pb = new ProcessBuilder(scriptPathHelper.getPathOfFile("", "deploiement_angular_build"),
					path, base, folderName);
			pb.start();
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				LOG.debug(line);
			}
		} catch (IOException e) {
			LOG.error(e);
			throw new ApplicationException(500, "Impossible de builder l'application Angular");
		}
	}

}
