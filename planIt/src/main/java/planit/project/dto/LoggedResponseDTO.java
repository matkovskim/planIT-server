package planit.project.dto;

public class LoggedResponseDTO {

	private String email;

	private String firstName;

	private String lastName;

	private String colour;

	public LoggedResponseDTO(String email, String firstName, String lastName, String colour) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.colour = colour;
	}

	LoggedResponseDTO() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
