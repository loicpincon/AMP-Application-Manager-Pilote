package application.manager.pilote.utilisateur.modele;

import lombok.Getter;

@Getter
public enum DroitApplicatifLevel {

	DEV("Developpeur"), EXPERT("Expert technique"), CP("Chef de projet"), PROP("proprietaire");

	private String libelle;

	private DroitApplicatifLevel(String libelle) {
		this.libelle = libelle;
	}

}
