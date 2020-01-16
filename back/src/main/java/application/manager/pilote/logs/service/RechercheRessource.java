package application.manager.pilote.logs.service;

import java.util.ArrayList;
import java.util.List;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RechercheRessource extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1059141293149934444L;

	private List<EnvironnementLog> envs = new ArrayList<>();

}
