package planit.project.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.TeamDTO;
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

	public boolean createTeam(TeamDTO createTeamDTO) {

		ApplicationUser creator = applicationUserRepository.findByEmail(createTeamDTO.getCreator());

		if (creator != null) {
			Team newTeam = new Team(createTeamDTO.getTitle(), createTeamDTO.getDescription(), creator);
			if (teamRepository.save(newTeam) != null) {
				for (String email : createTeamDTO.getMembers()) {
					if (addMember(createTeamDTO.getTitle(), email) == false) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return false;

	}

	public boolean addMember(String teamTitle, String email) {

		ApplicationUser user = applicationUserRepository.findByEmail(email);

		if (user == null) {
			return false;
		}

		Team team = teamRepository.findByTitle(teamTitle);

		if (team == null) {
			return false;
		}

		Set<ApplicationUser> members = team.getMembers();
		if (members.contains(user)) {
			return false;
		}

		members.add(user);
		team.setMembers(members);
		teamRepository.save(team);

		return true;

	}

	public boolean deleteTeam(Integer teamId) {
		Team team = teamRepository.findById((long) teamId).get();
		if (team != null) {
			teamRepository.delete(team);
			return true;
		}
		return false;
	}

	public TeamMemberResponseDTO addMember(TeamMemberDTO teamMemberDTO) {

		ApplicationUser user = applicationUserRepository.findByEmail(teamMemberDTO.getEmail());

		if (user == null) {
			return null;
		}

		Team team = teamRepository.findByTitle(teamMemberDTO.getTeamTitle());

		if (team == null) {
			return null;
		}

		Set<ApplicationUser> members = team.getMembers();
		if (members.contains(user)) {
			return null;
		}

		members.add(user);
		team.setMembers(members);
		teamRepository.save(team);

		TeamMemberResponseDTO response = new TeamMemberResponseDTO(user.getEmail(), user.getFirstName(),
				user.getLastName(), user.getColour());

		return response;
	}

	public TeamMemberResponseDTO checkMember(String email) {

		ApplicationUser user = applicationUserRepository.findByEmail(email);

		if (user == null) {
			return null;
		}

		TeamMemberResponseDTO response = new TeamMemberResponseDTO(user.getEmail(), user.getFirstName(),
				user.getLastName(), user.getColour());

		return response;

	}

	public boolean updateTeam(Integer teamId, TeamDTO teamDTO) {

		Optional<Team> optionalTeam = this.teamRepository.findById((long) teamId);
		if (optionalTeam.isPresent()) {
			Team team = optionalTeam.get();
			team.setTitle(teamDTO.getTitle());
			team.setDescription(teamDTO.getDescription());
			teamRepository.save(team);
			return true;
		}
		return false;

	}
	
	public boolean updateTeamMembers(Integer teamId, TeamDTO teamDTO) {
		
		Optional<Team> optionalTeam = this.teamRepository.findById((long) teamId);
		if (optionalTeam.isPresent()) {
			Team team = optionalTeam.get();
			Set<ApplicationUser> members = new HashSet<>();

			for (String email : teamDTO.getMembers()) {

				ApplicationUser user = applicationUserRepository.findByEmail(email);

				if (user == null) {
					return false;
				}

				if (members.contains(user)) {
					return false;
				}

				members.add(user);

			}
			
			team.setMembers(members);
			teamRepository.save(team);
			return true;
		}
		return false;
		
	}
	
	
}
