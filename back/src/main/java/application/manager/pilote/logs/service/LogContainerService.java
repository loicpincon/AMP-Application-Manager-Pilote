package application.manager.pilote.logs.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import application.manager.pilote.logs.modele.ApplicationLog;
import application.manager.pilote.logs.modele.EnvironnementLog;
import application.manager.pilote.logs.modele.InstanceLog;
import application.manager.pilote.logs.modele.LogMessage;
import application.manager.pilote.logs.modele.RechercheRessource;
import application.manager.pilote.server.modele.Server;
import application.manager.pilote.server.service.ServerService;

@Service
public class LogContainerService {

	protected static final Log LOG = LogFactory.getLog(LogContainerService.class);

	@Autowired
	private DockerClient dockerClient;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ServerService serverService;

	public List<LogMessage> getDockerLogs(String containerId, Date debut, Date fin) throws IOException {

		final List<LogMessage> logs = new ArrayList<>();

		LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
		logContainerCmd.withStdOut(true).withStdErr(true);
		if (debut != null) {
			Integer timestamp = Math.abs(Integer.valueOf((int) debut.getTime()));
			LOG.debug("auto : " + (int) (System.currentTimeMillis() / 1000));
			LOG.debug("cherche par date debut : " + timestamp);
			// logContainerCmd.withSince(timestamp);
		}
		// logContainerCmd.withTail(4); // get only the last 4 log entries

		logContainerCmd.withTimestamps(true);
		LogContainerResultCallback callback = new LogContainerResultCallback();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

		try {
			logContainerCmd.exec(new LogContainerResultCallback() {

				@SuppressWarnings("unused")
				@Override
				public void onNext(Frame item) {

					String logBrut = item.toString();
					if (logBrut.length() > 42) {

						try {
							String timestamp = logBrut.substring(8, 38).substring(0, 19);
							Date dateLog = sdf.parse(timestamp);
							LOG.debug("date de la log : " + dateLog.getTime());
							LOG.debug("date saisie deb : " + debut.getTime());
							LOG.debug("date saisie fin : " + fin.getTime());
							LOG.debug("compare : "
									+ (dateLog.getTime() < fin.getTime() && dateLog.getTime() > debut.getTime()));

							if (debut != null) {
								if (fin != null) {
									if (dateLog.getTime() < fin.getTime() && dateLog.getTime() > debut.getTime()) {
										LOG.debug("filtre debut et fin");

										String type = logBrut.substring(0, 6);
										String message = logBrut.substring(39, logBrut.length() - 1);
										logs.add(LogMessage.builder().timestamp(timestamp).type(type).message(message)
												.build());
									}
								} else {
									if (dateLog.getTime() > debut.getTime()) {
										String type = logBrut.substring(0, 6);
										String message = logBrut.substring(39, logBrut.length() - 1);
										logs.add(LogMessage.builder().timestamp(timestamp).type(type).message(message)
												.build());
									}
								}
							} else {
								String type = logBrut.substring(0, 6);
								String message = logBrut.substring(39, logBrut.length() - 1);
								logs.add(LogMessage.builder().timestamp(timestamp).type(type).message(message).build());
							}

						} catch (ParseException e) {
							LOG.error(e);
						}
					} else {
						logs.add(LogMessage.builder().message(logBrut).timestamp(logBrut.length() + "").type("ERROR")
								.build());

					}
				}
			}).awaitCompletion();
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		callback.close();
		return logs;
	}

	public RechercheRessource recupererRechercheRessourceUser(String idUser) {
		List<Application> apps = appService.recupererParUser(idUser);

		// POUR TOUTES LES APPLIS
		// POUR TOUS LES ENVS
		// POUR TOUTES LES INSTANCES

		RechercheRessource recherche = new RechercheRessource();
		for (Application app : apps) {

			Set<Integer> cles = app.getEnvironnements().keySet();
			Iterator<Integer> it = cles.iterator();

			while (it.hasNext()) {
				Integer cle = it.next();
				Server serveur = serverService.consulter(cle);

				Environnement valeur = app.getEnvironnements().get(cle);
				for (Instance i : valeur.getInstances()) {
					addEnvLog(recherche, cle, serveur.getNom(), app.getId(), app.getName(), i);
				}
			}
		}
		return recherche;
	}

	private void addEnvLog(RechercheRessource recherche, Integer idEnv, String nameEnv, String idAppp, String nameApp,
			Instance idIstance) {
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
				env2log.getApps().add(app2log);
			}
			InstanceLog il = new InstanceLog();
			il.setEtat(idIstance.getEtat());
			il.setId(idIstance.getContainerId());
			il.setLibelle(idIstance.getLibelle());
			app2log.getInstances().add(il);
		}
	}

}