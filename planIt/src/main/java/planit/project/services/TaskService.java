package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.Task;
import planit.project.repositories.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> firstSync(ApplicationUser user) {

		List<Task> list = this.taskRepository.findByUserAndDeleted(user, false);
		List<Task> teamTasks = this.taskRepository.findAllTeamTasks(user, false, false);
		if(teamTasks != null) {
			list.addAll(teamTasks);
		}

		if (list != null) {
			for (Task task : list) {
				if (task.getReminder() != null)
					task.setReminderId(task.getReminder().getId());

				if (task.getTeam() != null)
					task.setTeamId(task.getTeam().getId());
				
				if(task.getAssignee() != null) {
					task.setUserEmail(task.getAssignee().getEmail());
					task.setUserId(task.getAssignee().getId());
				}
			}
			return list;
		}

		return new ArrayList<>();

	}

	public List<Task> syncDate(ApplicationUser user, Date syncDate) {

		List<Task> personalTasks = this.taskRepository.findByUserAndModifyDateAfter(user, syncDate);
		List<Task> teamTasks = this.taskRepository.findAllSyncTeamTasks(user, syncDate);
		if(teamTasks != null) {
			personalTasks.addAll(teamTasks);
		}
		
		if (personalTasks != null) {
			for (Task task : personalTasks) {
				if (task.getReminder() != null)
					task.setReminderId(task.getReminder().getId());

				if (task.getTeam() != null)
					task.setTeamId(task.getTeam().getId());
				
				if(task.getAssignee() != null) {
					task.setUserEmail(task.getAssignee().getEmail());
					task.setUserId(task.getAssignee().getId());
				}
			}
			return personalTasks;
		}

		return new ArrayList<>();
	}
	
	public Task save(Task task) {
		return this.taskRepository.save(task);
	}

}
