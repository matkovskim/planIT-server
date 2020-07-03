package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.TeamDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.TeamUserConnection;
import planit.project.model.UserMessage;
import planit.project.repositories.ApplicationUserRepository;
import planit.project.repositories.TeamRepository;
import planit.project.repositories.TeamUserConnectionRepository;

@Service
public class TeamService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamUserConnectionRepository teamUserRepository;

	@Autowired
	private ChatService chatService;

	public Team findByIdAndDeleted(Long id) {
		return this.teamRepository.findByIdAndDeleted(id, false);
	}

	public boolean createTeam(TeamDTO createTeamDTO) {

		ApplicationUser creator = applicationUserRepository.findByEmail(createTeamDTO.getCreator());

		if (creator == null)
			return false;

		List<ApplicationUser> users = new ArrayList<>();

		for (String email : createTeamDTO.getMembers()) {
			ApplicationUser user;
			user = this.applicationUserRepository.findByEmail(email);
			if (user == null)
				return false;
			users.add(user);
		}

		Team newTeam = new Team(createTeamDTO.getTitle(), createTeamDTO.getDescription(), creator);
		newTeam = this.save(newTeam);
		if (newTeam == null)
			return false;

		for (ApplicationUser user : users) {
			if (addMember(newTeam, user) == false) {
				return false;
			}
		}
		return true;

	}

	public boolean addMember(Team team, ApplicationUser user) {

		TeamUserConnection conn = this.teamUserRepository.findIsMember(user, team, false);

		if (conn != null)
			return false;

		conn = new TeamUserConnection(team, user);
		conn = this.teamUserRepository.save(conn);

		return (conn == null) ? false : true;

	}

	public boolean deleteTeam(Integer teamId) {
		Team team = teamRepository.findById((long) teamId).get();
		if (team == null)
			return false;

		team.setDeleted(true);
		List<TeamUserConnection> members = this.teamUserRepository.findMembers(team, false);
		if (members != null) {
			for (TeamUserConnection conn : members) {
				conn.setDeleted(true);
				this.teamUserRepository.save(conn);
			}
		}
		List<UserMessage> messages = this.chatService.findByTeam(team);

		if (messages != null) {
			for (UserMessage message : messages) {
				message.setDeleted(true);
				this.chatService.save(message);
			}
		}

		this.teamRepository.save(team);

		return true;

	}

	public boolean updateTeam(Integer teamId, TeamDTO teamDTO) {

		Team team = this.teamRepository.findByIdAndDeleted((long) teamId, false);
		if (team == null)
			return false;

		team.setTitle(teamDTO.getTitle());
		team.setDescription(teamDTO.getDescription());
		teamRepository.save(team);
		return true;

	}

	public boolean updateTeamMembers(Integer teamId, TeamDTO teamDTO) {

		Team team = this.teamRepository.findByIdAndDeleted((long) teamId, false);

		if (team == null)
			return false;

		List<ApplicationUser> users = new ArrayList<>();

		for (String email : teamDTO.getMembers()) {
			ApplicationUser user;
			user = this.applicationUserRepository.findByEmail(email);
			if (user == null)
				return false;
			users.add(user);
		}

		List<TeamUserConnection> members = this.teamUserRepository.findMembers(team, false);
		if (members != null) {
			for (TeamUserConnection conn : members) {
				conn.setDeleted(true);
				this.teamUserRepository.save(conn);
			}
		}

		for (ApplicationUser user : users) {
			// TODO: check if it is the same?
			addMember(team, user);

		}

		return true;

	}

	public Team save(Team team) {
		return this.teamRepository.save(team);
	}

	public List<Team> firstSync(ApplicationUser user) {

		List<Team> list = this.teamRepository.findUserTeamsFirstSync(user, false, false);

		if (list != null) {
			for (Team team : list) {
				team.setCreatorEmail(team.getCreator().getEmail());
			}
			return list;
		}

		return new ArrayList<>();

	}

	public List<Team> syncDate(ApplicationUser user, Date syncDate) {

		List<Team> list = this.teamRepository.findUserTeamsSyncDate(user, syncDate);

		if (list != null) {
			for (Team team : list) {
				team.setCreatorEmail(team.getCreator().getEmail());
			}
			return list;
		}

		return new ArrayList<>();
	}

	public List<TeamUserConnection> firstSyncConn(ApplicationUser user) {

		List<Team> listTeam = this.teamUserRepository.findUserTeamsConnFirstSync(user, false, false);
		List<TeamUserConnection> members = new ArrayList<>();
		if (listTeam != null) {
			for (Team team : listTeam) {
				members.addAll(this.teamUserRepository.findMembers(team, false));
			}
			return members;
		}

		return new ArrayList<>();

	}

	public List<TeamUserConnection> syncDateConn(ApplicationUser user, Date syncDate) {

		List<Team> listTeam = this.teamUserRepository.findUserTeamsConnTeam(user, true, syncDate, false);
		List<TeamUserConnection> members = new ArrayList<>();
		if (listTeam != null) {
			for (Team team : listTeam) {
				members.addAll(this.teamUserRepository.findModifiedMembers(team, syncDate));
			}
			return members;
		}

		return new ArrayList<>();
	}

	public List<ApplicationUser> findMembers(Team team) {
		return this.teamUserRepository.findUserMembers(team, false);
	}

}
