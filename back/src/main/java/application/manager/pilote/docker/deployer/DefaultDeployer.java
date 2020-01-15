package application.manager.pilote.docker.deployer;

import java.util.Date;

import application.manager.pilote.application.modele.UserAction;
import application.manager.pilote.session.modele.UserSession;

public abstract class DefaultDeployer extends Thread {

	protected UserSession user;

	@Override
	public abstract void run();

	protected UserAction traceAction(String libelle, String status, String version) {
		UserAction us = new UserAction();
		us.setDate(new Date());
		us.setLibelle(libelle);
		us.setMembre(user.getNom() + " " + user.getPrenom());
		us.setStatus(status);
		us.setVersion(version);
		return us;
	}

	public void setUser(UserSession s) {
		this.user = s;
	}

}
