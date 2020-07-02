package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitDayConnection;
import planit.project.repositories.HabitDayConnectionRepository;

@Service
public class HabitDayConnectionService {

	@Autowired
	private HabitDayConnectionRepository repository;

	public List<HabitDayConnection> firstSync(ApplicationUser user) {

		List<HabitDayConnection> list = this.repository.findHabitDayConnectionFirstTime(false, false, user);

		if (list != null) {
			for (HabitDayConnection conn : list) {
				conn.setHabitId(conn.getHabit().getId());
			}
			return list;
		}

		return new ArrayList<>();
	}

	public List<HabitDayConnection> syncByDate(ApplicationUser user, Date date) {
		List<HabitDayConnection> list = this.repository.findHabitDayConnectionSync(date, user);

		if (list != null) {
			for (HabitDayConnection conn : list) {
				conn.setHabitId(conn.getHabit().getId());
			}
			return list;
		}

		return new ArrayList<>();
	}
	
	public HabitDayConnection save(HabitDayConnection conn) {
		return this.repository.save(conn);
	}
}
