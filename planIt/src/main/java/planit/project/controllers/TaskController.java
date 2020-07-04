package planit.project.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import planit.project.dto.LabelDTO;
import planit.project.dto.TaskDTO;
import planit.project.dto.TaskResponseDTO;
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

	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

	@PostMapping
	public ResponseEntity<?> createTask(@RequestBody @Valid TaskDTO taskDTO) {

		Task task = new Task();
		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());

		try {
			task.setStartDate(dbDateFormat.parse(taskDTO.getStartDate()));
		} catch (ParseException e) {

		}

		if (taskDTO.getStartTime() != null) {
			try {
				task.setStartTime(timeFormat.parse(taskDTO.getStartTime()));
			} catch (ParseException e) {

			}
		}

		task.setAddress(taskDTO.getAddress());
		task.setLongitude(taskDTO.getLongitude());
		task.setLatitude(taskDTO.getLatitude());
		task.setDone(taskDTO.isDone());
		task.setPriority(taskDTO.getPriority());

		TaskResponseDTO taskResponseDTO = new TaskResponseDTO();

		if (taskDTO.getReminderTime() != null) {
			Reminder reminder = new Reminder();
			reminder.setDate(taskDTO.getReminderTime());
			reminder.setDeleted(false);

			Reminder savedReminder = reminderService.save(reminder);
			if (savedReminder != null) {
				task.setReminder(savedReminder);
				taskResponseDTO.setReminderId(savedReminder.getId());
			}
		}

		if (taskDTO.getUserEmail() != null) {
			ApplicationUser user = userService.findByEmail(taskDTO.getUserEmail());
			if (user != null) {
				task.setUser(user);
			}
		}

		if (taskDTO.getTeamId() != null) {
			Team team = teamService.findByIdAndDeleted(taskDTO.getTeamId());
			if (team != null) {
				task.setTeam(team);
			}
		}

		Task savedTask = taskService.save(task);

		if (savedTask != null) {
			taskResponseDTO.setGlobalId(savedTask.getId());

			if (taskDTO.getLabels() != null && !taskDTO.getLabels().isEmpty()) {
				for (LabelDTO label : taskDTO.getLabels()) {
					if (label.getGlobalId() == null) {
						Label newLabel = new Label(label.getName(), label.getColor());
						Label savedLabel = labelService.save(newLabel);
						if (savedLabel != null) {
							label.setGlobalId(savedLabel.getId());

							TaskLabelConnection tlConnection = new TaskLabelConnection();
							tlConnection.setLabel(savedLabel);
							tlConnection.setTask(task);
							tlConnection.setDeleted(false);

							TaskLabelConnection savedTLConnection = taskLabelConnectionService.save(tlConnection);
							if (savedTLConnection != null) {
								label.setConnectionId(savedTLConnection.getId());
							}
						}

					} else {
						Label existingLabel = labelService.findByIdAndDeleted(label.getGlobalId());
						if (existingLabel != null) {

							TaskLabelConnection tlConnection = new TaskLabelConnection();
							tlConnection.setLabel(existingLabel);
							tlConnection.setTask(task);
							tlConnection.setDeleted(false);

							TaskLabelConnection savedTLConnection = taskLabelConnectionService.save(tlConnection);
							if (savedTLConnection != null) {
								label.setConnectionId(savedTLConnection.getId());
							}
						}
					}
				}
			}

			taskResponseDTO.setLabels(taskDTO.getLabels());

			return ResponseEntity.ok(taskResponseDTO);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

	@PutMapping("/{taskId}")
	public ResponseEntity<?> updateTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable Long taskId,
			@RequestParam(value = "date") Long date) {
		Task task = taskService.findByIdAndDeleted(taskId);
		TaskResponseDTO taskResponseDTO = new TaskResponseDTO();

		if (task == null) {
			return ResponseEntity.notFound().build();

		} else if (task.getModifyDate().getTime() > date) {
			return ResponseEntity.badRequest().build();
		}

		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());

		try {
			task.setStartDate(dbDateFormat.parse(taskDTO.getStartDate()));
		} catch (ParseException e) {

		}
		if (task.getStartTime() != null) {
			try {
				task.setStartTime(timeFormat.parse(taskDTO.getStartTime()));
			} catch (ParseException e) {

			}
		}
		task.setAddress(taskDTO.getAddress());
		task.setLongitude(taskDTO.getLongitude());
		task.setLatitude(taskDTO.getLatitude());
		task.setDone(taskDTO.isDone());
		task.setPriority(taskDTO.getPriority());

		if (taskDTO.getReminderTime() != null) {
			Reminder reminder = task.getReminder();
			if (reminder != null) {
				reminder.setDate(taskDTO.getReminderTime());
				taskResponseDTO.setReminderId(reminder.getId());
			} else {
				Reminder newReminder = new Reminder();
				newReminder.setDate(taskDTO.getReminderTime());
				newReminder.setDeleted(false);

				Reminder changedReminder = reminderService.save(newReminder);
				if (changedReminder != null) {
					taskResponseDTO.setReminderId(changedReminder.getId());
					task.setReminder(changedReminder);
				}

			}

		} else {
			Reminder reminder = task.getReminder();
			if (reminder != null) {
				task.setReminder(null);
				reminder.setDeleted(true);
				reminderService.save(reminder);
			} else {
				task.setReminder(null);
			}
		}

		if (taskDTO.getUserEmail() != null) {
			ApplicationUser user = userService.findByEmail(taskDTO.getUserEmail());
			if (user != null) {
				task.setUser(user);
			}
		} else {
			task.setUser(null);
		}

		Task savedTask = taskService.save(task);
		if (savedTask != null) {
			taskResponseDTO.setGlobalId(savedTask.getId());
			return ResponseEntity.ok(taskResponseDTO);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@DeleteMapping("/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
		Task task = taskService.findByIdAndDeleted(taskId);		
		if(task != null) {			
			Reminder reminder = task.getReminder();
			if(reminder != null) {
				reminder.setDeleted(true);
				reminderService.save(reminder);
			}
			
			task.setDeleted(true);
			taskService.save(task);
			
			List<TaskLabelConnection> tlConnections = taskLabelConnectionService.findByTaskAndDeleted(task);
			for(TaskLabelConnection tlc : tlConnections) {
				tlc.setDeleted(true);
				taskLabelConnectionService.save(tlc);
			}
			
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

}