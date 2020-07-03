package planit.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class HabitReminderConnection {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Transient
	private Long habitId;
	
	@Transient
	private Long reminderId;
	
	@JsonIgnore
	@ManyToOne
	private Habit habit;
	
	@JsonIgnore
	@ManyToOne
	private Reminder reminder;
	
	@Column
	private boolean deleted;
	
	@JsonIgnore
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@JsonIgnore
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;
	

	public HabitReminderConnection() {
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Habit getHabit() {
		return habit;
	}


	public void setHabit(Habit habit) {
		this.habit = habit;
	}


	public Reminder getReminder() {
		return reminder;
	}


	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}


	public Long getHabitId() {
		return habitId;
	}


	public void setHabitId(Long habitId) {
		this.habitId = habitId;
	}


	public Long getReminderId() {
		return reminderId;
	}


	public void setReminderId(Long reminderId) {
		this.reminderId = reminderId;
	}


	@Override
	public String toString() {
		return "HabitReminderConnection [id=" + id + ", habitId=" + habitId + ", reminderId=" + reminderId + ", habit="
				+ habit + ", reminder=" + reminder + ", deleted=" + deleted + ", createDate=" + createDate
				+ ", modifyDate=" + modifyDate + "]";
	}
	

}
