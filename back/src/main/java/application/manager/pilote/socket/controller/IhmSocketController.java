package application.manager.pilote.socket.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class IhmSocketController {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/ihm/connect")
	public void onConnection() {
		this.template.convertAndSend("/content/application", new Date());
	}

}
