package planit.project.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.Reminder;
import planit.project.repositories.ReminderRepository;

@Service
public class ReminderService {

	@Autowired
	private ReminderRepository repository;
	
	public List<Reminder> firstTask(ApplicationUser user) {
		return this.repository.findReminderFirstTimeTask(false, user, false);
	}

	public List<Reminder> syncByDateTask(ApplicationUser user, Date date) {
		return this.repository.findReminderSyncTask(date, user);
	}
	
	public Reminder save(Reminder reminder) {
		return this.repository.save(reminder);
	}

}
