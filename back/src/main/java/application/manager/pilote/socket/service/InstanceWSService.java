package application.manager.pilote.socket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import application.manager.pilote.application.modele.Instance;

@Service
public class InstanceWSService {

	private static final String URL_DETAILS_INSTANCE = "/content/application";

	@Autowired
	private SimpMessagingTemplate template;

	public void sendDetailsInstance(Instance ins) {
		this.template.convertAndSend(URL_DETAILS_INSTANCE, ins);
	}

}