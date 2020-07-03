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

import planit.project.dto.HabitSyncDTO;
import planit.project.model.ApplicationUser;
import planit.project.model.DayOfWeek;
import planit.project.model.Habit;
import planit.project.model.HabitDayConnection;
import planit.project.model.HabitFulfillment;
import planit.project.services.HabitDayConnectionService;
import planit.project.services.HabitFulfillmentService;
import planit.project.services.HabitReminderConnectionService;
import planit.project.services.HabitService;
import planit.project.services.ReminderService;
import planit.project.services.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/habit", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitController {

	@Autowired
	private HabitService habitService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReminderService reminderService;
	
	@Autowired
	private HabitReminderConnectionService habitReminderConnectionService;
	
	@Autowired
	private HabitFulfillmentService habitFullfilmentService;
	
	@Autowired
	private HabitDayConnectionService habitDayConnectionService;

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping("/sync")
	public ResponseEntity<?> synchronizationData(@RequestParam String email, @RequestParam(value = "date", required=false) Long date) {
		
		System.out.println(email);
		System.out.println(date);
		
		ApplicationUser user = null;
		
		if(email != null) {
			user = this.userService.findByEmail(email);
			if(user == null)
				return ResponseEntity.badRequest().build(); 
		}
		
		Date dateUserSync = null;
		HabitSyncDTO dto = new HabitSyncDTO();
		// if user sync date exists
		if (date != null) {
			
			dateUserSync = new Date(date);
			dto.setHabits(this.habitService.syncDate(user, dateUserSync));
			dto.setHabitReminderConnections(this.habitReminderConnectionService.syncByDate(user, dateUserSync));
			dto.setReminderConn(this.reminderService.syncByDateHabits(user, dateUserSync));
			dto.setHabitFulfillments(this.habitFullfilmentService.syncByDate(user, dateUserSync));
			dto.setHabitDayConnection(this.habitDayConnectionService.syncByDate(user, dateUserSync));
			System.out.println(dto);
			return ResponseEntity.ok(dto);
			
		}
		
		dto.setHabits(this.habitService.firstSync(user));
		dto.setHabitReminderConnections(this.habitReminderConnectionService.firstSync(user));
		dto.setReminderConn(this.reminderService.firstSyncHabits(user));
		dto.setHabitFulfillments(this.habitFullfilmentService.firstSync(user));
		dto.setHabitDayConnection(this.habitDayConnectionService.firstSync(user));
		System.out.println(dto);

		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/init")
	public ResponseEntity<?> init() {
		
		ApplicationUser user = this.userService.findByEmail("vesnam@gmail.com");
		
		Habit habit1 = new Habit("Running", "It is important to run", 125, 3);
		habit1.setUser(user);
		habit1 = this.habitService.save(habit1);
		Habit habit2 = new Habit("Running", "It is important to run", -1, -1);
		habit2.setUser(user);
		habit2 = this.habitService.save(habit2);
		HabitDayConnection conn = new HabitDayConnection();
		conn.setDay(DayOfWeek.MONDAY);
		conn.setHabit(habit2);
		this.habitDayConnectionService.save(conn);
		HabitFulfillment fulfillment = new HabitFulfillment();
		fulfillment.setDay(new Date());
		fulfillment.setHabit(habit2);
		this.habitFullfilmentService.save(fulfillment);
		
		Habit habit3 = new Habit("Running", "It is important to run", 200, 2);
		habit3.setUser(user);
		habit3 = this.habitService.save(habit3);
		
		return ResponseEntity.status(200).build();
		
	}

}
