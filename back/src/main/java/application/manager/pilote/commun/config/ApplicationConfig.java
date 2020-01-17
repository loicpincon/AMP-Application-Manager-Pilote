package application.manager.pilote.commun.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import application.manager.pilote.commun.helper.PropertiesReader;
import application.manager.pilote.commun.service.ShellService;
import application.manager.pilote.docker.helper.ScriptPathHelper;

@Configuration
public class ApplicationConfig {

	protected static final Log LOG = LogFactory.getLog(ApplicationConfig.class);

	@Autowired
	private ShellService shell;

	@Autowired
	private ScriptPathHelper pathHelper;

	@PostConstruct
	public void initShell() {
		if (SystemUtils.IS_OS_UNIX) {
			LOG.debug("Positionnement des droits d'execution sur le batch");
			String path = pathHelper.getRelativePath("script/unix/");
			shell.execute("chmod -R a+x " + path);
		}
	}

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
