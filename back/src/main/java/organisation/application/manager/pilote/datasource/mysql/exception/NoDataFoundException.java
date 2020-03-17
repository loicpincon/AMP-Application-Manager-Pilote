package organisation.application.manager.pilote.datasource.mysql.exception;

import org.springframework.http.HttpStatus;

import organisation.application.manager.pilote.commun.exception.ApplicationException;

public class NoDataFoundException extends ApplicationException {

	public NoDataFoundException(HttpStatus internalServerError, String message) {
		super(internalServerError, message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3171688702419654481L;

}
