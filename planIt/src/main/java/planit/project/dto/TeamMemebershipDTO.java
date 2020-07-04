package planit.project.dto;

public class TeamMemebershipDTO {

	private Long memebrshipId;
	private String userEmail;
	private Long teamId;
	
	public TeamMemebershipDTO() {
		
	}
	
	public TeamMemebershipDTO(Long globalId, String userEmail, Long teamId) {
		super();
		this.memebrshipId = globalId;
		this.userEmail = userEmail;
		this.teamId = teamId;
	}

	public Long getMemebrshipId() {
		return memebrshipId;
	}

	public void setMemebrshipId(Long memebrshipId) {
		this.memebrshipId = memebrshipId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	
	@Override
	public String toString() {
		return "TeamMemebershipDTO [memebrshipId=" + memebrshipId + ", userEmail=" + userEmail + ", teamId=" + teamId
				+ "]";
	}
	
}
