package application.manager.pilote.commun.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import application.manager.pilote.commun.helper.PropertiesReader;

@Configuration
public class ApplicationConfig {

	protected static final Log LOG = LogFactory.getLog(ApplicationConfig.class);

	@Bean
	public RestTemplate http() {
		return new RestTemplate();
	}

	@Bean
	public PropertiesReader properties() {
		return new PropertiesReader();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return new MappingJackson2HttpMessageConverter(mapper);
	}
}
