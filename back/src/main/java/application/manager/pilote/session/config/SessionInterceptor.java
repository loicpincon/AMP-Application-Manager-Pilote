package application.manager.pilote.session.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import application.manager.pilote.session.service.SessionService;

@Component
public class SessionInterceptor implements HandlerInterceptor {

	private static final Log LOG = LogFactory.getLog(SessionInterceptor.class);

	private static final String X_TOKEN_UTILISATEUR = "X-TOKEN-UTILISATEUR";

	@Autowired
	private SessionService sessionService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();
		LOG.debug("Filtrage requete entrante : (" + request.getMethod() + ")" + uri);
		String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR);
		if ("/session".equals(uri) || "/api/map".equals(uri)) {
			return true;
		} else {
			sessionService.getSession(tokenUserHeader);
			return true;
		}

	}

}
