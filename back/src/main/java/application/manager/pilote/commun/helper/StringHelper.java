package application.manager.pilote.commun.helper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StringHelper {

	protected static final Logger LOG = Logger.getLogger(StringHelper.class);

	/**
	 * 
	 * @param args
	 * @return
	 */
	public String concat(Object... args) {
		StringBuilder chaine = new StringBuilder();
		for (Object o : args) {
			chaine.append(o);
		}
		return chaine.toString();
	}

}
