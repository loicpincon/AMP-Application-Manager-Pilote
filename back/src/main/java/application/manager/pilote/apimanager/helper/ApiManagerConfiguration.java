package application.manager.pilote.apimanager.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import application.manager.pilote.apimanager.modele.Api;
import application.manager.pilote.apimanager.modele.ApiManager;

/**
 * Class permetant de construire API Manager
 * 
 * @author LoïcPinçon
 *
 */
@Component
public class ApiManagerConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ApiManagerConfiguration.class);

	private Map<String, Api> apiMap;

	@Autowired
	private HttpServletRequest req;

	/**
	 * 
	 * @return
	 */
	public Map<String, Api> getApiManager() {
		if (apiMap == null) {
			return apim(req);
		}
		return apiMap;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, Api> apim(HttpServletRequest req) {
		Map<String, Api> apiMapBuilder = new HashMap<>();
		logger.info("demarage du scan de pour l'api manager");
		String dns = req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath();
		Reflections ref = new Reflections("application");
		for (Class<?> cl : ref.getTypesAnnotatedWith(RequestMapping.class)) {
			RequestMapping classs = cl.getAnnotation(RequestMapping.class);
			ApiManager nomConttrolleur = cl.getAnnotation(ApiManager.class);
			for (Method m : cl.getDeclaredMethods()) {
				RequestMapping requestMethodMapping = m.getAnnotation(RequestMapping.class);
				String verbe = getVerbe(m);
				ApiManager nameapi = m.getAnnotation(ApiManager.class);
				if (nameapi != null) {
					String requestParam = buildParameterChain(m);
					requestParam = requestParam.substring(0, requestParam.length() - 1);
					String key = nomConttrolleur.value() + "." + nameapi.value();
					StringBuilder uri = new StringBuilder(classs.value()[0]);
					if (requestMethodMapping != null && requestMethodMapping.value().length > 0) {
						uri.append(requestMethodMapping.value()[0]);
					}
					uri.append(requestParam);
					apiMapBuilder.put(key, Api.builder().serveur(dns).key(key).url(dns + uri).uri(uri.toString())
							.verbe(verbe).build());
				}
			}
		}
		return apiMapBuilder;
	}

	/**
	 * 
	 * @param m
	 * @return
	 */
	private String buildParameterChain(Method m) {
		StringBuilder sb = new StringBuilder("?");
		for (Parameter p : m.getParameters()) {
			if (p.isAnnotationPresent(RequestParam.class)) {
				sb.append(p.getName());
				sb.append("={");
				sb.append(p.getName());
				sb.append("}&");
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param m
	 * @return
	 */
	private String getVerbe(Method m) {
		RequestMapping requestMethodMapping = m.getAnnotation(RequestMapping.class);
		GetMapping requestMethodGetMapping = m.getAnnotation(GetMapping.class);
		PostMapping requestMethodPostMapping = m.getAnnotation(PostMapping.class);
		PutMapping requestMethodPutMapping = m.getAnnotation(PutMapping.class);
		DeleteMapping requestMethodDeleteMapping = m.getAnnotation(DeleteMapping.class);
		String verbe = "";
		if (requestMethodMapping != null) {
			verbe = requestMethodMapping.method()[0].name();
		} else if (requestMethodGetMapping != null) {
			verbe = "GET";
		} else if (requestMethodPostMapping != null) {
			verbe = "POST";
		} else if (requestMethodPutMapping != null) {
			verbe = "PUT";
		} else if (requestMethodDeleteMapping != null) {
			verbe = "DELETE";
		}
		return verbe;
	}
}
