package application.manager.pilote.docker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import application.manager.pilote.commun.helper.PropertiesReader;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

@Component
public class DockerConfig {

	/**
	 * 
	 */
	private static final String UNIX_VAR_RUN_DOCKER_SOCK_DEFAULT = "unix:////var/run/docker.sock";

	/**
	 * 
	 */
	private static final String UNIX_VAR_RUN_DOCKER_SOCK = "UNIX_VAR_RUN_DOCKER_SOCK";

	@Autowired
	private PropertiesReader properties;

	/**
	 * 
	 * @return
	 */
	@Bean
	public DockerClient dockerClient() {
		return DockerClientBuilder.getInstance(dockerConfig()).withDockerCmdExecFactory(dockerCmdExecFactory()).build();
	}

	/**
	 * 
	 * @return
	 */
	private DockerClientConfig dockerConfig() {
		return DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost(
						properties.getPropertyOrElse(UNIX_VAR_RUN_DOCKER_SOCK, UNIX_VAR_RUN_DOCKER_SOCK_DEFAULT))
				.build();
	}

	/**
	 * 
	 * @return
	 */
	private DockerCmdExecFactory dockerCmdExecFactory() {
		return new JerseyDockerCmdExecFactory();
	}
}
