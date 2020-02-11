package organisation.application.manager.pilote.socket.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;

@RestController
@RequestMapping("/api/socket")
@ApiManager("Socket")
public class SocketApi {

	@Autowired
	private SimpMessagingTemplate template;

	@GetMapping
	public void recuperer() {
		this.template.convertAndSend("/content/organisation.application", new Date());
	}
}
