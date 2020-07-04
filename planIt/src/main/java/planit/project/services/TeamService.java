package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.dto.TeamDTO;
import planit.project.dto.TeamMemberDTO;
import planit.project.dto.TeamMemebershipDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Reminder;
import planit.project.model.Task;
import planit.project.model.TaskLabelConnection;
import planit.project.model.Team;
import planit.project.model.TeamUserConnection;
import planit.project.model.UserMessage;
import planit.project.repositories.ApplicationUserRepository;
import planit.project.repositories.ReminderRepository;
import planit.project.repositories.TaskLabelConnectionRepository;
import planit.project.repositories.TaskRepository;
import planit.project.repositories.TeamRepository;
import planit.project.repositories.TeamUserConnectionRepository;

@Service
public class TeamService {

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TeamUserConnectionRepository teamUserRepository;

	@Autowired
	private TaskLabelConnectionRepository taskLabelConnectionRepository;

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private ChatService chatService;

	public Team findByIdAndDeleted(Long id) {
		return this.teamRepository.findByIdAndDeleted(id, false);
	}

	public List<TeamMemebershipDTO> createTeam(TeamDTO createTeamDTO) {

		List<TeamMemebershipDTO>teamMemebershipList = new ArrayList<>();
		
		ApplicationUser creator = applicationUserRepository.findByEmail(createTeamDTO.getCreator());

		if (creator == null)
			return null;

		List<ApplicationUser> users = new ArrayList<>();

		for (String email : createTeamDTO.getMembers()) {
			ApplicationUser user;
			user = this.applicationUserRepository.findByEmail(email);
			if (user == null)
				return null;
			users.add(user);
		}

		Team newTeam = new Team(createTeamDTO.getTitle(), createTeamDTO.getDescription(), creator);
		newTeam = this.save(newTeam);
		if (newTeam == null)
			return null;

		for (ApplicationUser user : users) {
			TeamMemebershipDTO teamMemebershipDTO = addMember(newTeam, user);
			if (teamMemebershipDTO == null) {
				return null;
			}
			teamMemebershipList.add(teamMemebershipDTO);
		}
		return teamMemebershipList;

	}

	public TeamMemebershipDTO addMember(Team team, ApplicationUser user) {

		TeamUserConnection conn = this.teamUserRepository.findIsMember(user, team, false);

		if (conn != null)
			return null;

		conn = new TeamUserConnection(team, user);
		conn = this.teamUserRepository.save(conn);
		
		TeamMemebershipDTO teamMemebrshepDTO = new TeamMemebershipDTO(conn.getId(), conn.getUser().getEmail(), conn.getTeam().getId());
		System.out.println(teamMemebrshepDTO);

		return (teamMemebrshepDTO == null) ? null : teamMemebrshepDTO;

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

		List<Task> tasks = taskRepository.findByTeam(team);
		if (tasks != null) {
			for (Task task : tasks) {
				Reminder reminder = task.getReminder();
				if (reminder != null) {
					reminder.setDeleted(true);
					reminderRepository.save(reminder);
				}
				List<TaskLabelConnection> connections = taskLabelConnectionRepository.findByTask(task);
				if (connections != null) {
					for (TaskLabelConnection con : connections) {
						con.setDeleted(true);
						taskLabelConnectionRepository.save(con);
					}
				}

				task.setDeleted(true);
				this.taskRepository.save(task);
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

	public List<TeamMemebershipDTO> updateTeamMembers(Integer teamId, TeamDTO teamDTO) {

		List<TeamMemebershipDTO>teamMemebershipList = new ArrayList<>();
		
		Team team = this.teamRepository.findByIdAndDeleted((long) teamId, false);

		if (team == null)
			return null;

		List<ApplicationUser> users = new ArrayList<>();

		for (String email : teamDTO.getMembers()) {
			ApplicationUser user;
			user = this.applicationUserRepository.findByEmail(email);
			if (user == null)
				return null;
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
			TeamMemebershipDTO teamMemebershipDTO = addMember(team, user);
			teamMemebershipList.add(teamMemebershipDTO);
		}
	

		return teamMemebershipList;

	}

	public Team save(Team team) {
		return this.teamRepository.save(team);
	}

	public List<Team> firstSync(ApplicationUser user) {

		List<Team> list = this.teamRepository.findUserTeamsFirstSync(user, false, false);

		if (list != null) {
			for (Team team : list) {
				System.out.println("DA LI SA< ODRISAN? "+ team.isDeleted());
				team.setCreatorEmail(team.getCreator().getEmail());
				team.setCreatorId(team.getCreator().getId());
			}
			return list;
		}

		return new ArrayList<>();

	}

	public List<Team> syncDate(ApplicationUser user, Date syncDate) {

		List<Team> list = this.teamRepository.findUserTeamsSyncDate(user, syncDate);

		if (list != null) {
			for (Team team : list) {
				System.out.println("DA LI SA< ODRISAN? "+ team.isDeleted());

				team.setCreatorEmail(team.getCreator().getEmail());
				team.setCreatorId(team.getCreator().getId());
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
				List<TeamUserConnection> conn = this.teamUserRepository.findMembers(team, false);
				for (TeamUserConnection c : conn) {
					c.setTeamId(team.getId());
				}
				members.addAll(conn);
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
				List<TeamUserConnection> conn = this.teamUserRepository.findModifiedMembers(team, syncDate);
				for (TeamUserConnection c : conn) {
					c.setTeamId(team.getId());
				}
				members.addAll(conn);
			}

			return members;
		}

		return new ArrayList<>();
	}

	public List<ApplicationUser> findMembers(Team team) {
		return this.teamUserRepository.findUserMembers(team, false);
	}

}
