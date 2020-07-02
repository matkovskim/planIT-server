package planit.project.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import planit.project.model.ApplicationUser;
import planit.project.model.Habit;
import planit.project.repositories.HabitRepository;

@Service
public class HabitService {

	@Autowired
	private HabitRepository habitRepository;

	public Habit getOne(Long id) {
		Optional<Habit> medicine = this.habitRepository.findById(id);
		if (medicine.isPresent())
			return medicine.get();
		return null;
	}

	public List<Habit> findAll() {
		return this.habitRepository.findAll();
	}

	public Habit save(Habit habit) {
		return this.habitRepository.save(habit);
	}

	public boolean delete(Long id) {
		Habit habit = this.getOne(id);
		if (habit != null) {
			habit.setDeleted(true);

			return true;
		}
		return false;
	}

	public Habit update(Long id, Habit habit) {
		Habit oldHabit = this.getOne(id);

		if (oldHabit == null)
			return null;

		oldHabit.setDescription(habit.getDescription());
		oldHabit.setGoal(habit.getGoal());
		oldHabit.setNumberOfDays(habit.getNumberOfDays());
		oldHabit.setTitle(habit.getTitle());
		oldHabit.setDeleted(habit.isDeleted());

		return this.save(oldHabit);
	}

	public List<Habit> firstSync(ApplicationUser user) {
		return this.habitRepository.findByUserAndDeleted(user, false);
	}

	public List<Habit> syncDate(ApplicationUser user, Date syncDate) {
		return this.habitRepository.findByUserAndModifyDateAfter(user, syncDate);
	}

}
