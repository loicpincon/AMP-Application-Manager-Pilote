package application.manager.pilote.commun.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Date;

import org.springframework.http.HttpStatus;

import application.manager.pilote.commun.modele.Retour;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8131465557308920951L;

	private final HttpStatus status;

	public ApplicationException() {
		super();
		this.status = INTERNAL_SERVER_ERROR;
	}

	/**
	 * 
	 * @param message
	 */
	public ApplicationException(String message) {
		super(message);
		this.status = INTERNAL_SERVER_ERROR;
	}

	/**
	 * 
	 * @param message
	 */
	public ApplicationException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	/**
	 * 
	 * @param message
	 */
	public ApplicationException(int status, String message) {
		super(message);
		this.status = HttpStatus.valueOf(status);
	}

	public Retour getBody() {
		return Retour.builder().timestamp(new Date()).libelleStatus(this.status).codeHttp(this.status.value())
				.message(super.getMessage()).build();
	}

}
