package planit.project.dto;

import java.util.List;

import planit.project.model.Label;
import planit.project.model.Reminder;
import planit.project.model.Task;
import planit.project.model.TaskLabelConnection;


public class TaskSyncDTO {

	private List<Task> tasks;
	
	private List<Label> labels;
	
	private List<TaskLabelConnection> taskLabelConnections;
	
	private List<Reminder> taskReminders;
	
	public TaskSyncDTO() {}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public List<TaskLabelConnection> getTaskLabelConnections() {
		return taskLabelConnections;
	}

	public void setTaskLabelConnections(List<TaskLabelConnection> taskLabelConnections) {
		this.taskLabelConnections = taskLabelConnections;
	}

	public List<Reminder> getTaskReminders() {
		return taskReminders;
	}

	public void setTaskReminders(List<Reminder> taskReminders) {
		this.taskReminders = taskReminders;
	}
	
	

}
