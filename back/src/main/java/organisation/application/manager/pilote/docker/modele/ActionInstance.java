package organisation.application.manager.pilote.docker.modele;

public enum ActionInstance {
	DEPLOY("D", "Deployé"), STOP("S", "Arret"), RELOAD("R", "Redemmarage"), DELETE("DEL", "Supprimmer");

	private String code;

	private String libelle;

	private ActionInstance(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}

	public String getLibelle() {
		return this.libelle;
	}

	public String getCode() {
		return this.code;
	}
}
