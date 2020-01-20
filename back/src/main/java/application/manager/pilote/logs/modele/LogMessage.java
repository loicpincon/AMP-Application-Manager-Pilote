package application.manager.pilote.logs.modele;

import application.manager.pilote.commun.modele.BasicDataBean;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogMessage extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6338424588148331096L;

	@Builder.Default
	String type = "";

	@Builder.Default
	String timestamp = "";

	@Builder.Default
	String message = "";

}
