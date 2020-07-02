package planit.project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.MessageDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.UserMessage;
import planit.project.repositories.ApplicationUserRepository;
import planit.project.repositories.TeamRepository;
import planit.project.repositories.UserMessageRepository;

@Service
public class ChatService {

	@Autowired
	private UserMessageRepository userMessageRepository;

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private TeamRepository teamRepository;

	public boolean sendMessage(MessageDTO messageDTO) {

		ApplicationUser sender = applicationUserRepository.findByEmail(messageDTO.getSender());

		Optional<Team> optionalTeam = teamRepository.findById(messageDTO.getServerTeamId());

		if (!optionalTeam.isPresent()) {
			return false;
		}

		if (sender != null) {
			UserMessage newMessage = new UserMessage(messageDTO.getMessage(), messageDTO.getCreatedAt(), sender,
					optionalTeam.get());
			if (userMessageRepository.save(newMessage) != null) {
				return true;
			}
			return false;
		}
		return false;

	}

	public Team getTeamById(Long teamId) {
		Optional<Team> team = teamRepository.findById(teamId);
		if (team.isPresent()) {
			return team.get();
		}
		return null;
	}

	public List<MessageDTO> getMessages(Integer teamId) {

		List<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();

		Optional<Team> team = teamRepository.findById(teamId.longValue());
		if (team.isPresent()) {
			List<UserMessage> messages = userMessageRepository.findAllByTeam(team.get());
			if (messages != null) {
				for (UserMessage m : messages) {
					MessageDTO newMessage = new MessageDTO(m.getMessage(), m.getCreatedAt(), m.getSender().getEmail(),
							teamId.longValue());
					messageDTOs.add(newMessage);
				}
				return messageDTOs;
			}
			return null;
		}
		return null;
	}

}
