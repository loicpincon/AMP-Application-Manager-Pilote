package application.manager.pilote.commun.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class DefaultService {

	protected static final Log LOG = LogFactory.getLog(DefaultService.class);

	@Autowired
	protected RestTemplate http;

}
