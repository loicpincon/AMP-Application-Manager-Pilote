package application.manager.pilote.commun.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import application.manager.pilote.commun.modele.Retour;

@Component
public class GlobalErrorApplicationHandler extends AbstractHandlerExceptionResolver {

	protected static final Log LOG = LogFactory.getLog(GlobalErrorApplicationHandler.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (ex instanceof ApplicationException) {
			return handleIllegalArgument((ApplicationException) ex, response);
		}
		if (ex instanceof NoSuchElementException) {
			return handleIllegalArgument(ex, NOT_FOUND, response);
		}
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		LOG.error(ex.getMessage(), ex);
		return handleIllegalArgument(ex, INTERNAL_SERVER_ERROR, response);
	}

	private ModelAndView handleIllegalArgument(Exception exc, HttpStatus ex, HttpServletResponse response) {
		try {
			response.setStatus(ex.value());
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(buildRetour(ex, exc).toJson());
			out.flush();
		} catch (IOException e) {
			LOG.error(e);
		}
		return new ModelAndView();
	}

	private ModelAndView handleIllegalArgument(ApplicationException ex, HttpServletResponse response) {
		try {
			response.setStatus(ex.getStatus().value());
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(buildRetour(ex.getStatus(), ex).toJson());
			out.flush();
		} catch (IOException e) {
			LOG.error(e);
		}
		return new ModelAndView();
	}

	private Retour buildRetour(HttpStatus ex, Exception exc) {
		Retour r = new Retour();
		r.setCodeHttp(ex.value());
		r.setMessage(exc.getMessage());
		r.setLibelleStatus(ex);
		r.setTimestamp(new Date());
		return r;
	}

}
