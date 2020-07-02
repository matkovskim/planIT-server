package planit.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String message;

	@Column
	private Long createdAt;

	@ManyToOne
	private ApplicationUser sender;

	@ManyToOne
	private Team team;

	public UserMessage() {

	}

	public UserMessage(String message, Long createdAt, ApplicationUser sender, Team team) {
		super();
		this.message = message;
		this.createdAt = createdAt;
		this.sender = sender;
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUser getSender() {
		return sender;
	}

	public void setSender(ApplicationUser sender) {
		this.sender = sender;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
