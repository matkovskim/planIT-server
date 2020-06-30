package planit.project.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import planit.project.utils.SendPushNotification;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

	@GetMapping("/pushMessage")
	public String sendPushNotificatoin() {
		try {
			SendPushNotification.pushFCMNotification("chat");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Done";
	}

}
