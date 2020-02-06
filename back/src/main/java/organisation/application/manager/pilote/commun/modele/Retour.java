package organisation.application.manager.pilote.commun.modele;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Retour extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5631684070145509345L;

	private Date timestamp;

	private HttpStatus libelleStatus;

	private Integer codeHttp;

	private String message;

}
