package organisation.application.manager.pilote.application.modele;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisation.application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Livrable extends BasicDataBean implements Comparable<Livrable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566283723638659039L;

	private String id;

	private String pathtoFile;

	private String nom;

	private Date dateUpload;

	private boolean isFolder;

	@Override
	public int compareTo(Livrable o) {
		return this.dateUpload.compareTo(o.getDateUpload());
	}

}
