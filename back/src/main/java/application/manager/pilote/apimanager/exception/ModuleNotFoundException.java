package application.manager.pilote.apimanager.exception;

import application.manager.pilote.commun.exception.ApplicationException;

/**
 * Class Exception
 * 
 * @author loicpincon
 *
 */
public class ModuleNotFoundException extends ApplicationException {
	
	private static final long serialVersionUID = -8146650744598280576L;

	/**
	 * Declenchée si le module n'est pas trouvée
	 * @param message
	 */
	public ModuleNotFoundException(String message) {
		super(message);
	}
}
