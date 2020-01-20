package application.manager.pilote.commun.mail;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	protected static final Log LOG = LogFactory.getLog(MailService.class);

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(String to, String subject, String msg) {
		new Thread() {
			@Override
			public void run() {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name()); // (3)
				try {
					helper.setFrom("AMP <projet.secret3133@gmail.com>");
					helper.setTo(to);
					helper.setSubject(subject);
					helper.setText(msg);
				} catch (MessagingException e) {
					LOG.error(e);
				}
				mailSender.send(mimeMessage);
			}
		}.start();

	}
}
