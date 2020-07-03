package planit.project.dto;

public class TeamMemberDTO {

	private Long id;
	private String email;

	public TeamMemberDTO(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public TeamMemberDTO() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
