package planit.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	String title;

	@Column
	String description;

	@ManyToOne
	ApplicationUser creator;

	@ManyToMany
	Set<ApplicationUser> members;

	public Team(String title, String description, ApplicationUser creator) {
		super();
		this.title = title;
		this.description = description;
		this.creator = creator;
		this.members = new HashSet<>();
	}

	public Team() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ApplicationUser getCreator() {
		return creator;
	}

	public void setCreator(ApplicationUser creator) {
		this.creator = creator;
	}

	public Set<ApplicationUser> getMembers() {
		return members;
	}

	public void setMembers(Set<ApplicationUser> members) {
		this.members = members;
	}

}
