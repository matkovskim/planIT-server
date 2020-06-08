package planit.project.dto;

public class CreateTeamDTO {
	
	String title;
	String description;
	String creator;
	
	public CreateTeamDTO() {
		
	}
	
	public CreateTeamDTO(String title, String description, String creator) {
		super();
		this.title = title;
		this.description = description;
		this.creator = creator;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

}
