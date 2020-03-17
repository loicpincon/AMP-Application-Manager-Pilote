package organisation.application.manager.pilote.session.config;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import organisation.application.manager.pilote.commun.exception.ApplicationException;
import organisation.application.manager.pilote.session.modele.SecuredLevel;
import organisation.application.manager.pilote.session.modele.UserSession;
import organisation.application.manager.pilote.session.service.SessionService;

@Component
public class SessionInterceptor implements HandlerInterceptor {

	private static final Log LOG = LogFactory.getLog(SessionInterceptor.class);

	private static final String X_TOKEN_UTILISATEUR = "X-TOKEN-UTILISATEUR";

	private static final String X_TOKEN_UTILISATEUR_UPLOAD = "X-TOKEN-UTILISATEUR-UPLOAD";

	@Autowired
	private SessionService sessionService;

	@Autowired
	@Qualifier("listeApis")
	public Map<String, SecuredLevel> methods;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String controllerName = "";
		String actionName = "";
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			controllerName = handlerMethod.getBeanType().getSimpleName();
			actionName = handlerMethod.getMethod().getName();
			SecuredLevel levelToSecure = methods.get(handlerMethod.getMethod().toGenericString());
			if (levelToSecure != null) {
				LOG.trace("API securise : " + controllerName + "." + actionName + " avec niveau --> "
						+ levelToSecure.name());

				if (levelToSecure == SecuredLevel.ADMIN) {
					// TODO rajouter un cas de session avec admin
				} else if (levelToSecure == SecuredLevel.UPLOAD_VERSION_APP) {
					String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR_UPLOAD);
					if (tokenUserHeader == null) {
						tokenUserHeader = "";
					}
					sessionService.isTokenUserValid(tokenUserHeader);
				} else if (levelToSecure == SecuredLevel.MEMBRE) {
					String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR);
					sessionService.getSession(tokenUserHeader);
				} else if (levelToSecure == SecuredLevel.CONSULTER_APP) {
					@SuppressWarnings("unchecked")
					Map<String, String> path = (Map<String, String>) request
							.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
					String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR);
					UserSession user = sessionService.getSession(tokenUserHeader);
					if (!user.getCodesApplications().contains(path.get("idApp"))) {
						throw new ApplicationException(FORBIDDEN, "Vous n'avez pas les droits sur cette application");
					}

				}else if(levelToSecure == SecuredLevel.CONSULTER_BASE_ADMIN) {
					@SuppressWarnings("unchecked")
					Map<String, String> path = (Map<String, String>) request
							.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
					String tokenUserHeader = request.getHeader(X_TOKEN_UTILISATEUR);
					UserSession user = sessionService.getSession(tokenUserHeader);
					if (!user.getCodesApplications().contains(path.get("id"))) {
						throw new ApplicationException(FORBIDDEN, "Vous n'avez pas les droits sur cette application");
					}else {
					
					}
				}
			}
		}
		return true;
	}

}
