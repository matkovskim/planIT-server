package planit.project.dto;

public class TeamMemberResponseDTO {

	private String email;
	private String name;
	private String lastName;
	private String colour;

	public TeamMemberResponseDTO() {
		super();
	}

	public TeamMemberResponseDTO(String email, String name, String lastName, String colour) {
		super();
		this.email = email;
		this.name = name;
		this.lastName = lastName;
		this.colour = colour;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

}
