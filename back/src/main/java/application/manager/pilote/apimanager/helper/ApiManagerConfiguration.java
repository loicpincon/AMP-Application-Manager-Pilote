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

	Map<String, Api> apiMap;

	@Autowired
	private HttpServletRequest req;

	public Map<String, Api> getApiManager() {
		if (apiMap == null) {
			return apim(req);
		}
		return apiMap;
	}

	public Map<String, Api> apim(HttpServletRequest req) {
		Map<String, Api> apiMap = new HashMap<>();
		logger.info("demarage du scan de pour l'api manager");

		String dns = "";

		if (req != null) {
			dns = req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath();
		}

		Reflections ref = new Reflections("projet");
		for (Class<?> cl : ref.getTypesAnnotatedWith(RequestMapping.class)) {
			RequestMapping classs = cl.getAnnotation(RequestMapping.class);
			ApiManager nomConttrolleur = cl.getAnnotation(ApiManager.class);
			for (Method m : cl.getDeclaredMethods()) {
				RequestMapping findable = m.getAnnotation(RequestMapping.class);
				ApiManager nameapi = m.getAnnotation(ApiManager.class);
				if (nameapi != null) {
					String requestParam = "?";
					for (Parameter p : m.getParameters()) {
						if (p.isAnnotationPresent(RequestParam.class)) {
							requestParam += p.getName();
							requestParam += "={";
							requestParam += p.getName();
							requestParam += "}&";
						}

					}
					requestParam = requestParam.substring(0, requestParam.length() - 1);

					String key = nomConttrolleur.value() + "." + nameapi.value();
					String uri = "";
					if (findable.value().length > 0) {
						uri = classs.value()[0] + findable.value()[0];
					} else {
						uri = classs.value()[0];
					}
					uri += requestParam;

					String verb = findable.method()[0].name();

					apiMap.put(key,
							Api.builder().serveur(dns).key(key).url(dns.toString() + uri).uri(uri).verbe(verb).build());
				}
			}
		}
		return apiMap;
	}
}
