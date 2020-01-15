package application.manager.pilote.docker.deployer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import application.manager.pilote.application.modele.UserAction;
import application.manager.pilote.session.service.SessionService;

public abstract class DefaultDeployer extends Thread {

	@Autowired
	private SessionService sessionService;

	@Override
	public abstract void run();

	protected UserAction traceAction(String libelle, String status, String version) {
		UserAction us = new UserAction();
		us.setDate(new Date());
		us.setLibelle(libelle);
		us.setMembre(sessionService.getSession().getNom() + " " + sessionService.getSession().getPrenom());
		us.setStatus(status);
		us.setVersion(version);
		return us;
	}

}
