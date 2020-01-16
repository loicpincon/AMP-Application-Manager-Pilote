package application.manager.pilote.logs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import application.manager.pilote.application.modele.Application;
import application.manager.pilote.application.modele.Environnement;
import application.manager.pilote.application.modele.Instance;
import application.manager.pilote.application.service.ApplicationService;
import application.manager.pilote.session.service.SessionService;

@Service
public class LogContainerService {

	protected static final Log LOG = LogFactory.getLog(LogContainerService.class);

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private SessionService sessionService;

	private int lastLogTime = (int) (System.currentTimeMillis() / 1000);

	private static String nameOfLogger = "dockertest.PrintContainerLog";

	private static Logger myLogger = Logger.getLogger(nameOfLogger);

	public List<LogMessage> getDockerLogs(String containerId) throws IOException {

		final List<LogMessage> logs = new ArrayList<>();

		LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
		logContainerCmd.withStdOut(true).withStdErr(true);
		// logContainerCmd.withSince(lastLogTime); // UNIX timestamp (integer) to filter
		// logs. Specifying a timestamp
		// will
		// only output log-entries since that timestamp.
		// logContainerCmd.withTail(4); // get only the last 4 log entries

		logContainerCmd.withTimestamps(true);

		LogContainerResultCallback callback = new LogContainerResultCallback();

		try {
			logContainerCmd.exec(new LogContainerResultCallback() {

				@Override
				public void onNext(Frame item) {
					String logBrut = item.toString();
					if (logBrut.length() > 42) {
						String type = logBrut.substring(0, 6);
						String timestamp = logBrut.substring(8, 38).substring(0, 19);
						String message = logBrut.substring(39, logBrut.length() - 1);
						logs.add(LogMessage.builder().timestamp(timestamp).type(type).message(message).build());

					} else {
						logs.add(LogMessage.builder().message(logBrut).timestamp(logBrut.length() + "").type("ERROR")
								.build());

					}

				}
			}).awaitCompletion();
		}

		catch (InterruptedException e) {
			myLogger.severe("Interrupted Exception!" + e.getMessage());
		}

		lastLogTime = (int) (System.currentTimeMillis() / 1000) + 5; // assumes at least a 5 second wait between calls
																		// to getDockerLogs
		callback.close();
		return logs;
	}

	public RechercheRessource recupererRechercheRessourceUser(String idUser) {
		List<Application> apps = appService.recupererParUser(idUser);
		RechercheRessource recherche = new RechercheRessource();
		for (Application app : apps) {
			Set<Integer> cles = app.getEnvironnements().keySet();
			Iterator<Integer> it = cles.iterator();
			while (it.hasNext()) {
				Integer cle = it.next();
				Environnement valeur = app.getEnvironnements().get(cle);
				for (Instance i : valeur.getInstances()) {
					addEnvLog(recherche, cle, "Name Of Serveur", app.getId(), app.getName(), i.getContainerId());
				}
			}
		}
		return recherche;
	}

	private void addEnvLog(RechercheRessource recherche, Integer idEnv, String nameEnv, String idAppp, String nameApp,
			String idIstance) {
		if (idIstance != null) {
			EnvironnementLog env2log = null;
			boolean isFind = false;
			for (EnvironnementLog envLog : recherche.getEnvs()) {
				if (envLog.getIdEnv().equals(idEnv)) {
					env2log = envLog;
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				env2log = new EnvironnementLog();
				env2log.setIdEnv(idEnv);
				env2log.setLibelle(nameEnv);
				recherche.getEnvs().add(env2log);
			}

			boolean isFindApp = false;

			ApplicationLog app2log = null;

			for (ApplicationLog app2logB : env2log.getApps()) {

				if (app2logB.getId().equals(idAppp)) {
					LOG.debug("je connais cette appli");
					app2log = app2logB;
					isFindApp = true;
					break;
				}
			}
			if (!isFindApp) {
				app2log = new ApplicationLog();
				app2log.setId(idAppp);
				app2log.setName(nameApp);
			}

			InstanceLog il = new InstanceLog();
			il.setId(idIstance);
			app2log.getInstances().add(il);
			env2log.getApps().add(app2log);
		}

	}

}