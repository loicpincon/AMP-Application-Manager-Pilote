package application.manager.pilote.commun.modele;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BasicDataBean implements Serializable {

	private final Log LOG = LogFactory.getLog(getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1243192863345975369L;

	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			LOG.error(e);
			return null;
		}
	}

}
