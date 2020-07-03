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

		if (list != null) {
			for (Task task : list) {
				if (task.getReminder() != null)
					task.setReminderId(task.getReminder().getId());

				if (task.getTeam() != null)
					task.setTeamId(task.getTeam().getId());
				
				if(task.getUser() != null)
					task.setUserEmail(task.getUser().getEmail());
			}
			return list;
		}

		return new ArrayList<>();

	}

	public List<Task> syncDate(ApplicationUser user, Date syncDate) {

		List<Task> list = this.taskRepository.findByUserAndModifyDateAfter(user, syncDate);

		if (list != null) {
			for (Task task : list) {
				if (task.getReminder() != null)
					task.setReminderId(task.getReminder().getId());

				if (task.getTeam() != null)
					task.setTeamId(task.getTeam().getId());
				
				if(task.getUser() != null)
					task.setUserEmail(task.getUser().getEmail());
			}
			return list;
		}

		return new ArrayList<>();
	}
	
	public Task save(Task task) {
		return this.taskRepository.save(task);
	}

}
