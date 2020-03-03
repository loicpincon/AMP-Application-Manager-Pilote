package organisation.application.manager.pilote.datasource;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6308958573976352194L;

	private String type;

	private String containerId;

}
