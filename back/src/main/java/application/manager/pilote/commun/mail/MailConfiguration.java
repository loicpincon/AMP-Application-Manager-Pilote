package application.manager.pilote.commun.mail;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {

	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl mailer = new JavaMailSenderImpl();
		mailer.setHost("smtp.gmail.com");
		mailer.setPort(587);
		mailer.setUsername("projet.secret3133@gmail.com");
		mailer.setPassword("knrgtvwozydcgjij");
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		mailer.setJavaMailProperties(props);
		return mailer;
	}

}
