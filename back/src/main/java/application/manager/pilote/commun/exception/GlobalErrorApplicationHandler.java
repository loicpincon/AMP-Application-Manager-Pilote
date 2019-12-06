package application.manager.pilote.commun.exception;

import java.io.IOException;
import java.io.PrintWriter;
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
		LOG.error(ex);
		if (ex instanceof ApplicationException) {
			return handleIllegalArgument((ApplicationException) ex, response, request);
		}
		if (ex instanceof NoSuchElementException) {
			return handleIllegalArgument((ApplicationException) ex, HttpStatus.NOT_FOUND, response, request);
		}
		if (ex instanceof Exception) {
			return handleIllegalArgument(ex, HttpStatus.INTERNAL_SERVER_ERROR, response, request);
		}
		return null;
	}

	private ModelAndView handleIllegalArgument(Exception exc, HttpStatus ex, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			response.setStatus(ex.value());
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Retour r = new Retour();
			r.setCodeHttp(ex.value());
			r.setMessage(exc.getMessage());
			r.setLibelleStatus(ex);
			r.setTimestamp(new Date());
			LOG.info(r.toJson());
			out.print(r.toJson());
			out.flush();
		} catch (IOException e) {
			LOG.error(e);
		}
		return new ModelAndView();
	}

	private ModelAndView handleIllegalArgument(ApplicationException ex, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			response.setStatus(ex.getStatus().value());
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Retour r = new Retour();
			r.setCodeHttp(ex.getStatus().value());
			r.setMessage(ex.getMessage());
			r.setLibelleStatus(ex.getStatus());
			r.setTimestamp(new Date());
			out.print(r.toJson());
			out.flush();
		} catch (IOException e) {
			LOG.error(e);
		}
		return new ModelAndView();
	}

}
