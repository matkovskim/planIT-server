package planit.project.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.CreateTeamDTO;
import planit.project.dto.TeamMemberDTO;
import planit.project.dto.TeamMemberResponseDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.repositories.ApplicationUserRepository;
import planit.project.repositories.TeamRepository;

@Service
public class TeamService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private TeamRepository teamRepository;

	public boolean createTeam(CreateTeamDTO createTeamDTO) {

		ApplicationUser creator = applicationUserRepository.findByEmail(createTeamDTO.getCreator());

		if (creator != null) {
			Team newTeam = new Team(createTeamDTO.getTitle(), createTeamDTO.getDescription(), creator);
			if(teamRepository.save(newTeam)!=null) {
				return true;
			}
			return false;
		}

		return false;

	}

	public TeamMemberResponseDTO addMember(TeamMemberDTO teamMemberDTO) {

		ApplicationUser user=applicationUserRepository.findByEmail(teamMemberDTO.getEmail());
		
		if(user==null) {
			return null;
		}
		
		Team team=teamRepository.findByTitle(teamMemberDTO.getTeamTitle());
		
		if(team==null) {
			return null;
		}
		
		Set<ApplicationUser>members=team.getMembers();
		if(members.contains(user)) {
			return null;
		}
		
		members.add(user);
		team.setMembers(members);
		teamRepository.save(team);
		
		TeamMemberResponseDTO response=new TeamMemberResponseDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getColour());
		
		return response;
	}

}
