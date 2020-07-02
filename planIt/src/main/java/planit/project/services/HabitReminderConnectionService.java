package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitReminderConnection;
import planit.project.repositories.HabitReminderConnectionRepository;

@Service
public class HabitReminderConnectionService {

	@Autowired
	private HabitReminderConnectionRepository repository;

	public List<HabitReminderConnection> firstSync(ApplicationUser user) {
		List<HabitReminderConnection> list = this.repository.findHabitReminderConnectionsFirstTime(false, false, user);
		if (list != null) {
			for (HabitReminderConnection conn : list) {
				conn.setHabitId(conn.getHabit().getId());
				conn.setReminderId(conn.getReminderId());
			}
			return list;
		}

		return new ArrayList<>();
	}

	public List<HabitReminderConnection> syncByDate(ApplicationUser user, Date date) {
		
		List<HabitReminderConnection> list = this.repository.findHabitReminderConnectionsSync(date, user);
		if (list != null) {
			for (HabitReminderConnection conn : list) {
				conn.setHabitId(conn.getHabit().getId());
				conn.setReminderId(conn.getReminderId());
			}
			return list;
		}

		return new ArrayList<>();
	}
	
	public HabitReminderConnection save(HabitReminderConnection conn) {
		return this.repository.save(conn);
	}

}
