package application.manager.pilote.session.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import application.manager.pilote.session.modele.Secured;
import application.manager.pilote.session.modele.SecuredLevel;

@Configuration

public class MethodSecureScannerConfig {

	/**
	 * 
	 * @return
	 */
	@Bean(name = "listeApis")
	public Map<String, SecuredLevel> methods() {
		Map<String, SecuredLevel> mes = new HashMap<>();
		Reflections ref = new Reflections("application");
		for (Class<?> cl : ref.getTypesAnnotatedWith(RestController.class)) {
			for (Method m : cl.getDeclaredMethods()) {
				if (!m.toGenericString().contains("private")) {
					Secured securedAnnot = m.getAnnotation(Secured.class);
					if (securedAnnot != null && securedAnnot.level() != null) {
						mes.put(m.toGenericString(), securedAnnot.level());
					}
				}

			}
		}
		return mes;

	}
}
