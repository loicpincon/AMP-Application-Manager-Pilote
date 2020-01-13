package application.manager.pilote.logs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

@Service
public class LogContainerService {

	@Autowired
	private DockerClient dockerClient;

	private int lastLogTime = (int) (System.currentTimeMillis() / 1000);

	private static String nameOfLogger = "dockertest.PrintContainerLog";

	private static Logger myLogger = Logger.getLogger(nameOfLogger);

	public List<LogMessage> getDockerLogs(String containerId) {

		final List<LogMessage> logs = new ArrayList<>();

		LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
		logContainerCmd.withStdOut(true).withStdErr(true);
		// logContainerCmd.withSince(lastLogTime); // UNIX timestamp (integer) to filter logs. Specifying a timestamp
		// will
		// only output log-entries since that timestamp.
		// logContainerCmd.withTail(4); // get only the last 4 log entries

		logContainerCmd.withTimestamps(true);

		LogContainerResultCallback callback = new LogContainerResultCallback();

		try {
			logContainerCmd.exec(new LogContainerResultCallback() {

				@Override
				public void onNext(Frame item) {
					String logBrut = item.toString();
					if (logBrut.length() > 42) {
						String type = logBrut.substring(0, 8);
						String timestamp = logBrut.substring(9, 39);
						String message = logBrut.substring(40, logBrut.length() - 2);
						logs.add(LogMessage.builder().timestamp(timestamp).type(type).message(message).build());

					}
					else {
						logs.add(LogMessage.builder().timestamp(logBrut.length() + "").type("ERROR").build());

					}

				}
			}).awaitCompletion();
		}

		catch (InterruptedException e) {
			myLogger.severe("Interrupted Exception!" + e.getMessage());
		}

		lastLogTime = (int) (System.currentTimeMillis() / 1000) + 5; // assumes at least a 5 second wait between calls
																		// to getDockerLogs

		return logs;
	}
}