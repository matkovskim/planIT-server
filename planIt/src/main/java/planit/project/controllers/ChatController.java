package planit.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.MessageDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.services.ChatService;
import planit.project.services.TeamService;
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

	@Autowired
	private TeamService teamService;

	@PostMapping("/message")
	public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {

		if (chatService.sendMessage(messageDTO)) {
			try {
				ApplicationUser sender = userService.findByEmail(messageDTO.getSender());
				if (sender != null) {
					Team team = chatService.getTeamById(messageDTO.getServerTeamId());
					if (team != null) {
						List<ApplicationUser> members = teamService.findMembers(team);
						for (ApplicationUser member : members) {
							if (!member.getEmail().equals(messageDTO.getSender())) {
								String firstLastName = sender.getFirstName() + " " + sender.getLastName();
								SendPushNotification.pushFCMNotification(firstLastName, member.getFirebaseId(),
										team.getTitle(), messageDTO);
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

	@GetMapping("/all")
	public ResponseEntity<List<MessageDTO>> checkUser(@RequestParam Long teamId) {
		List<MessageDTO> messages = chatService.getMessages(teamId);
		if (messages != null) {
			return new ResponseEntity<>(messages, HttpStatus.OK);
		}
		return ResponseEntity.status(400).build();
	}

}
