package planit.project.dto;

import java.util.List;
import java.util.Set;

import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.TeamUserConnection;
import planit.project.model.UserMessage;

public class TeamSyncDTO {

	private List<Team> teams;

	private List<TeamUserConnection> teamUserConnections;

	private List<UserMessage> messages;
	
	private Set<ApplicationUser> users;

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<TeamUserConnection> getTeamUserConnections() {
		return teamUserConnections;
	}

	public void setTeamUserConnections(List<TeamUserConnection> teamUserConnections) {
		this.teamUserConnections = teamUserConnections;
	}

	public List<UserMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<UserMessage> messages) {
		this.messages = messages;
	}

	public Set<ApplicationUser> getUsers() {
		return users;
	}

	public void setUsers(Set<ApplicationUser> users) {
		this.users = users;
	}
	
	

}
