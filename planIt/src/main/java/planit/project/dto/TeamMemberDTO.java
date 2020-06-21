package planit.project.dto;

public class TeamMemberDTO {

	private String teamTitle;
	private String email;

	public TeamMemberDTO(String teamTitle, String email) {
		super();
		this.teamTitle = teamTitle;
		this.email = email;
	}

	public TeamMemberDTO() {

	}

	public String getTeamTitle() {
		return teamTitle;
	}

	public void setTeamTitle(String teamTitle) {
		this.teamTitle = teamTitle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
