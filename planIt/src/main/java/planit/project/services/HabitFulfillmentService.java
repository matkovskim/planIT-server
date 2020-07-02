package planit.project.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitFulfillment;
import planit.project.repositories.HabitFulfillmentRepository;

@Service
public class HabitFulfillmentService {

	@Autowired
	private HabitFulfillmentRepository habitFulfillmentRepository;

	public List<HabitFulfillment> firstSync(ApplicationUser user) {
		List<HabitFulfillment> list = this.habitFulfillmentRepository.findHabitFillmentsFirstTime(false, false, user);

		if (list != null) {
			for (HabitFulfillment conn : list) {
				conn.setHabitId(conn.getHabit().getId());
			}
			return list;
		}

		return new ArrayList<>();

	}

	public List<HabitFulfillment> syncByDate(ApplicationUser user, Date date) {
		List<HabitFulfillment> list = this.habitFulfillmentRepository.findHabitFillmentsSync(date, user);

		if (list != null) {
			for (HabitFulfillment conn : list) {
				conn.setHabitId(conn.getHabit().getId());
			}
			return list;
		}

		return new ArrayList<>();
	}
	
	public HabitFulfillment save(HabitFulfillment habitFulfillment) {
		return this.habitFulfillmentRepository.save(habitFulfillment);
	}

}
