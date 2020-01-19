package application.manager.pilote.apimanager.exception;

import application.manager.pilote.commun.exception.ApplicationException;

/**
 * Class Exception
 * 
 * @author loicpincon
 *
 */
public class ApiNotFoundException extends ApplicationException {

	private static final long serialVersionUID = -8146650744598280536L;

	/**
	 * Declenchée si une API n'est pas trouvée
	 * @param message
	 */
	public ApiNotFoundException(String message) {
		super(message);
	}

}
