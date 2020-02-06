package organisation.application.manager.pilote.commun.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class ShellService extends DefaultService {

	protected static final Log LOG = LogFactory.getLog(ShellService.class);

	/**
	 * 
	 * @param command
	 */
	public void execute(String command) {
		try {
			LOG.debug(command);
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("bash", "-c", command);

			Process process = processBuilder.start();
			int exitVal = process.waitFor();

			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			if (exitVal == 0) {
				if (!output.toString().equals("")) {
					LOG.debug(output.toString());
				}
			}
		} catch (IOException | InterruptedException e) {
			LOG.error(e);
		}
	}

}
