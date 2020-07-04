package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.Task;
import planit.project.model.TaskLabelConnection;
import planit.project.repositories.TaskLabelConnectionRepository;

@Service
public class TaskLabelConnectionService {

	@Autowired
	private TaskLabelConnectionRepository repository;
	
	public List<TaskLabelConnection> firstSync(ApplicationUser user) {

		List<TaskLabelConnection> list = this.repository.findTaskLabelConnectionFirstTime(false, false, user, false);

		if (list != null) {
			for (TaskLabelConnection conn : list) {
				conn.setLabelId(conn.getLabel().getId());
				conn.setTaskId(conn.getTask().getId());
			}
			return list;
		}

		return new ArrayList<>();
	}

	public List<TaskLabelConnection> syncByDate(ApplicationUser user, Date date) {
		List<TaskLabelConnection> list = this.repository.findTaskLabelConnectionSync(date, user);

		if (list != null) {
			for (TaskLabelConnection conn : list) {
				conn.setLabelId(conn.getLabel().getId());
				conn.setTaskId(conn.getTask().getId());
			}
			return list;
		}

		return new ArrayList<>();
	}
	
	public TaskLabelConnection save(TaskLabelConnection conn) {
		return this.repository.save(conn);
	}
	
	public List<TaskLabelConnection> findByTaskAndDeleted(Task task){
		return repository.findByTaskAndDeleted(task, false);
	}

}
