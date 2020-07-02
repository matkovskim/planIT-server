package planit.project.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.MessageDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.services.ChatService;
import planit.project.services.UserService;
import planit.project.utils.SendPushNotification;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/message")
	public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {

		if (chatService.sendMessage(messageDTO)) {
			try {
				ApplicationUser sender = userService.findByEmail(messageDTO.getSender());
				if(sender!=null) {
					Team team = chatService.getTeamById(messageDTO.getServerTeamId());
					if(team!=null) {
						Set<ApplicationUser> members=team.getMembers();
						for(ApplicationUser member:members) {
							if(!member.getEmail().equals(messageDTO.getSender())){
								String firstLastName = sender.getFirstName()+" "+sender.getLastName();
								SendPushNotification.pushFCMNotification(firstLastName, member.getFirebaseId(), team.getTitle(), messageDTO);
							}
						}
					}
				}
			} catch (Exception e) {
				return ResponseEntity.status(400).build();
			}
			return ResponseEntity.status(200).build();
		}

		return ResponseEntity.status(400).build();

	}
	
}
