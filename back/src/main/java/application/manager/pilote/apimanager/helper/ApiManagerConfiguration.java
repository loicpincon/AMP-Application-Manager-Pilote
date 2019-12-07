package application.manager.pilote.apimanager.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	/**
	 * 
	 * @return
	 */
	public Map<String, Api> getApiManager(HttpServletRequest req) {
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
	private Map<String, Api> apim(HttpServletRequest req) {
		Map<String, Api> apiMapBuilder = new HashMap<>();
		logger.info("demarage du scan de pour l'api manager");
		String dns = req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath();
		Reflections ref = new Reflections("application");
		for (Class<?> cl : ref.getTypesAnnotatedWith(RequestMapping.class)) {
			RequestMapping classs = cl.getAnnotation(RequestMapping.class);
			ApiManager nomConttrolleur = cl.getAnnotation(ApiManager.class);
			for (Method m : cl.getDeclaredMethods()) {
				Api api = Api.builder().serveur(dns).build();
				buildApi(api, m);
				ApiManager nameapi = m.getAnnotation(ApiManager.class);
				if (nameapi != null) {
					String requestParam = buildParameterChain(m);
					requestParam = requestParam.substring(0, requestParam.length() - 1);
					if (nameapi.value().equals("")) {
						api.setKey(nomConttrolleur.value() + "." + m.getName());
					} else {
						api.setKey(nomConttrolleur.value() + "." + nameapi.value());
					}
					StringBuilder uri = new StringBuilder(classs.value()[0]);
					uri.append(api.getUri());
					uri.append(requestParam);
					api.setUri(uri.toString());
					api.setUrl(api.getServeur() + api.getUri());
					apiMapBuilder.put(api.getKey(), api);
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
	private String buildApi(Api a, Method m) {
		RequestMapping requestMethodMapping = m.getAnnotation(RequestMapping.class);
		GetMapping requestMethodGetMapping = m.getAnnotation(GetMapping.class);
		PostMapping requestMethodPostMapping = m.getAnnotation(PostMapping.class);
		PutMapping requestMethodPutMapping = m.getAnnotation(PutMapping.class);
		DeleteMapping requestMethodDeleteMapping = m.getAnnotation(DeleteMapping.class);
		String verbe = "";
		a.setUri("");
		if (requestMethodMapping != null) {
			a.setVerbe(requestMethodMapping.method()[0].name());
			a.setUri(getPath(requestMethodMapping.value()));
		} else if (requestMethodGetMapping != null) {
			a.setVerbe("GET");
			if (requestMethodGetMapping.path() != null) {
				a.setUri(getPath(requestMethodGetMapping.path()));
			}
		} else if (requestMethodPostMapping != null) {
			a.setVerbe("POST");
			if (requestMethodPostMapping.path() != null) {
				a.setUri(getPath(requestMethodPostMapping.path()));
			}
		} else if (requestMethodPutMapping != null) {
			a.setVerbe("PUT");
			if (requestMethodPutMapping.path() != null) {
				a.setUri(getPath(requestMethodPutMapping.path()));
			}
		} else if (requestMethodDeleteMapping != null) {
			a.setVerbe("DELETE");
			if (requestMethodDeleteMapping.path() != null) {
				a.setUri(getPath(requestMethodDeleteMapping.path()));
			}
		}
		return verbe;
	}

	private String getPath(String[] tab) {
		if (tab != null && tab.length > 0) {
			return tab[0];
		}
		return "";
	}
}
