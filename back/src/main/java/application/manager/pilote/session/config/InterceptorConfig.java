package application.manager.pilote.session.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import application.manager.pilote.commun.helper.PropertiesReader;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	SessionInterceptor sessionInterceptor;

	@Autowired
	private PropertiesReader properties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (Boolean.valueOf(properties.getPropertyOrElse("SESSION_SECURE", true))) {
			registry.addInterceptor(sessionInterceptor);
		}
	}
}
