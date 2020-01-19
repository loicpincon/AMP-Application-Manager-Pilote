package application.manager.pilote.application.modele;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
public class Livrable extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566283723638659039L;

	private String id;

	private String pathtoFile;

	private String nom;

	private Date dateUpload;

	private boolean isFolder;

}
