package application.manager.pilote.docker.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.modele.ParametreSeries;
import application.manager.pilote.application.modele.WarApplication;
import application.manager.pilote.commun.exception.ApplicationException;
import application.manager.pilote.docker.service.pr.ContainerParam;
import application.manager.pilote.server.modele.Server;
import lombok.Builder;

public class DockerWarDeployer extends DefaultDeployer<WarApplication> {

	protected static final Log LOG = LogFactory.getLog(DockerWarDeployer.class);

	Environnement envChoisi;

	ContainerParam param;

	Server server;

	@Builder
	public DockerWarDeployer(Application app, Instance ins, Environnement envChosi, Server server,
			ContainerParam param) {
		super((WarApplication) app, ins);
		this.envChoisi = envChosi;
		this.param = param;
		this.server = server;
	}

	/**
	 * @param serveur
	 * @return
	 */
	protected Ports getPortsBinds(Instance ins) {
		Ports portBindings = new Ports();
		ExposedPort expoPort = new ExposedPort(8080);
		portBindings.bind(expoPort, Binding.bindPort(Integer.valueOf(ins.getPort())));
		return portBindings;
	}

	public void run() {
		try {
			ParametreSeries parametres = null;

			for (ParametreSeries paramB : envChoisi.getParametres()) {
				if (paramB.getVersion().equals(param.getVersionParam())) {
					parametres = paramB;
					break;
				}
			}

			if (ins.getEtat().equals("L") || ins.getEtat().equals("S")) {
				this.dockerContainerService.manage(app.getId(), server.getId(), ins.getId(), "delete");
			}

			String pathToWar = properties.getProperty(BASE_PATH_TO_APPLICATION_STOCK) + SLASH + app.getId() + SLASH
					+ param.getVersion() + SLASH + app.getBaseName();

			File copied = new File(genererCheminTemporaire() + SLASH + app.getBaseName());
			FileUtils.copyFile(new File(pathToWar), copied);
			deployfileHelper.createGcpFile(genererCheminTemporaire() + "/" + app.getNomFichierProperties(),
					parametres.getParametres());

			ProcessBuilder pb = new ProcessBuilder(scriptPathHelper.getPathOfFile("", "deploiement_war_gcp"),
					genererCheminTemporaire(), genererCheminTemporaire() + SLASH + app.getBaseName(),
					genererCheminTemporaire() + SLASH + "ROOT.war" + " .");

			pb.start();
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = br.readLine()) != null) {
				LOG.debug(line);
			}

			Map<String, String> params = new HashMap<>();
			params.put("basename", app.getBaseName());
			createContainer(params);

			ins.setEtat("L");
			if (server.getDns() != null) {
				ins.setUrl("http://" + server.getDns() + ":" + ins.getPort());
			} else {
				ins.setUrl("http://" + server.getIp() + ":" + ins.getPort());
			}
			ins.setVersionApplicationActuel(param.getVersion());
			ins.setVersionParametresActuel("0.0.0");
			ins.getUserActions().add(traceAction("Deploy", "Succes", param.getVersion()));
			template.convertAndSend("/content/application", ins);
			LOG.debug("fin du thread");
			appService.modifier(app);

		} catch (DockerException | IOException | NullPointerException e) {
			LOG.error(e);
			ins.setEtat("S");
			ins.getUserActions().add(traceAction("Deploy", "Echec", param.getVersion()));
			appService.modifier(app);
			template.convertAndSend("/content/application", ins);
			throw new ApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
