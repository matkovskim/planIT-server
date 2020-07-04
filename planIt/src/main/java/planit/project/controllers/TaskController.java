package planit.project.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.TaskSyncDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.Label;
import planit.project.model.Reminder;
import planit.project.model.Task;
import planit.project.model.TaskLabelConnection;
import planit.project.model.TaskPriority;
import planit.project.model.Team;
import planit.project.services.LabelService;
import planit.project.services.ReminderService;
import planit.project.services.TaskLabelConnectionService;
import planit.project.services.TaskService;
import planit.project.services.TeamService;
import planit.project.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private LabelService labelService;

	@Autowired
	private TaskLabelConnectionService taskLabelConnectionService;

	@Autowired
	private ReminderService reminderService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat formatTime = new SimpleDateFormat("HH:mm");

	@GetMapping("/sync")
	public ResponseEntity<?> synchronizationData(@RequestParam String email,
			@RequestParam(value = "date", required = false) Long date) {

		System.out.println(email);
		System.out.println(date);

		ApplicationUser user = null;

		if (email != null) {
			user = this.userService.findByEmail(email);
			if (user == null)
				return ResponseEntity.badRequest().build();
		}

		Date dateUserSync = null;
		TaskSyncDTO dto = new TaskSyncDTO();
		// if user sync date exists
		if (date != null) {

			dateUserSync = new Date(date);

			dto.setTasks(this.taskService.syncDate(user, dateUserSync));
			dto.setLabels(this.labelService.syncByDate(user, dateUserSync));
			dto.setTaskLabelConnections(this.taskLabelConnectionService.syncByDate(user, dateUserSync));
			dto.setTaskReminders(this.reminderService.syncByDateTask(user, dateUserSync));
			System.out.println(dto);
			return ResponseEntity.ok(dto);

		}

		dto.setTasks(this.taskService.firstSync(user));
		dto.setLabels(this.labelService.firstSync(user));
		dto.setTaskLabelConnections(this.taskLabelConnectionService.firstSync(user));
		dto.setTaskReminders(this.reminderService.firstTask(user));
		System.out.println(dto);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/init")
	public ResponseEntity<?> init() throws Exception {

		ApplicationUser user = this.userService.findByEmail("vesnam@gmail.com");

		Reminder reminder = new Reminder();
		reminder.setDate("23:52");
		reminder = this.reminderService.save(reminder);

		Task task = new Task("Task 1", "Opis", "address", format.parse("2020-06-22"), format.parse("2020-07-27"), false,
				false, TaskPriority.HIGH, null, null, null, reminder, user);
		task = this.taskService.save(task);

		Label label = new Label();
		label.setName("vekica");
		label.setColor("142");

		label = this.labelService.save(label);

		TaskLabelConnection conn = new TaskLabelConnection();
		conn.setTask(task);
		conn.setLabel(label);
		conn.setTaskId(task.getId());
		conn.setLabelId(label.getId());
		this.taskLabelConnectionService.save(conn);

		return ResponseEntity.status(200).build();
	}

}