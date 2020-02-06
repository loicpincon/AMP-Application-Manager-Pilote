package organisation.application.manager.pilote.docker.modele;

public enum EtatInstance {

	L("Demarrée"), S("Stoppée"), P("En manipulation"), V("Vide");

	private String libelle;

	private EtatInstance(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return this.libelle;
	}

}
