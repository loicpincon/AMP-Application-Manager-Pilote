package application.manager.pilote.session.modele;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import application.manager.pilote.commun.modele.BasicDataBean;
import application.manager.pilote.utilisateur.modele.DroitApplicatif;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSession extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3589374037449861898L;

	@Id
	private String token;

	private String nom;

	private String prenom;

	private List<DroitApplicatif> rights;

	@JsonIgnore
	public List<String> getCodesApplications() {
		List<String> temp = new ArrayList<>();
		for (DroitApplicatif da : rights) {
			temp.add(da.getApplicationId());
		}
		return temp;
	}
}
