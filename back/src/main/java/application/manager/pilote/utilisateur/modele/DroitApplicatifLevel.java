package application.manager.pilote.utilisateur.modele;

import lombok.Getter;

@Getter
public enum DroitApplicatifLevel {

	DEV("Developpeur", true, false, false, false, false), EXPERT("Expert technique", true, true, false, true, true),
	CP("Chef de projet", true, true, false, false, false), PROP("proprietaire", true, true, true, true, true);

	private String libelle;

	private boolean read;

	private boolean update;

	private boolean delete;

	private boolean pilote;

	private boolean admin;

	private DroitApplicatifLevel(String libelle, boolean read, boolean update, boolean delete, boolean pilote,
			boolean admin) {
		this.libelle = libelle;
		this.read = read;
		this.update = update;
		this.admin = admin;
		this.delete = delete;
		this.pilote = pilote;
	}
	
	/**
	 * Permet de verifier la presence d'un droit
	 * @param level
	 * @return
	 */
	public static Boolean isPresent(String level) {
		for (DroitApplicatifLevel da : values()) {
			if (da.name().equals(level)) {
				return true;
			}
		}
		return false;
	}

}
