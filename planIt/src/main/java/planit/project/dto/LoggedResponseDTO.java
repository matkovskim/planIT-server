package planit.project.dto;

public class LoggedResponseDTO {

	private String email;

	private String name;

	private String lastName;

	private String colour;

	private String firebaseId;

	public LoggedResponseDTO(String email, String firstName, String lastName, String colour, String firebaseId) {
		super();
		this.email = email;
		this.name = firstName;
		this.lastName = lastName;
		this.colour = colour;
		this.firebaseId = firebaseId;
	}

	LoggedResponseDTO() {

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

	public void setName(String firstName) {
		this.name = firstName;
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

	public String getFirebaseId() {
		return firebaseId;
	}

	public void setFirebaseId(String firebaseId) {
		this.firebaseId = firebaseId;
	}

}
