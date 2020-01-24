package application.manager.pilote.commun.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import application.manager.pilote.commun.service.ShellService;
import application.manager.pilote.docker.helper.ScriptPathHelper;

@Configuration
public class ScriptDeploiementConfig {

	protected static final Log LOG = LogFactory.getLog(ScriptDeploiementConfig.class);

	@Autowired
	private ShellService shell;

	@Autowired
	private ScriptPathHelper pathHelper;

	@PostConstruct
	public void initShell() {
		if (SystemUtils.IS_OS_UNIX) {
			LOG.debug("Positionnement des droits d'execution sur le batch");
			String path = pathHelper.getRelativePath("/script/unix/");
			shell.execute("chmod -R a+x " + path);
		} else if (SystemUtils.IS_OS_WINDOWS) {
			LOG.debug("Positionnement des droits d'execution sur le batch");
			String path = pathHelper.getRelativePath("/script/windows/");
			LOG.debug(path);
		}
	}

}
