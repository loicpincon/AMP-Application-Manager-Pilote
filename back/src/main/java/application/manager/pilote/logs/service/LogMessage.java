package application.manager.pilote.logs.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import application.manager.pilote.commun.modele.BasicDataBean;

@Getter
@Setter
@Builder
public class LogMessage extends BasicDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6338424588148331096L;

	String type = "";

	String timestamp = "";

	String message = "";

}
