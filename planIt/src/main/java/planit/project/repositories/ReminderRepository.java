package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Reminder;

@Repository
public interface ReminderRepository  extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

	Reminder findByIdAndDeleted(Long id, boolean deleted);

	List<Reminder> findByModifyDateAfter(Date date);

	List<Reminder> findByDeleted(boolean deleted);
	
	@Query("SELECT r FROM HabitReminderConnection hc inner join hc.habit as h inner join hc.reminder as r WHERE hc.deleted = ?1 and h.deleted = ?2 and r.deleted = ?4 and h.user = ?3")
	List<Reminder> findReminderFirstTimeHabit(boolean deletedConnection, boolean deletedHabit, ApplicationUser user, boolean deletedReminder);
	
	@Query("SELECT r FROM HabitReminderConnection hc inner join hc.habit as h inner join hc.reminder as r WHERE h.user = ?2 and r.modifyDate > ?1")
	List<Reminder> findReminderSyncHabit(Date lastUpdated, ApplicationUser user);
	
	
	@Query("SELECT r FROM Task t inner join t.reminder as r WHERE t.user = ?2 and t.deleted = ?1 and r.deleted = ?3")
	List<Reminder> findReminderFirstTimeTask(boolean deletedTask, ApplicationUser user, boolean deletedReminder);
	
	@Query("SELECT r FROM Task t inner join t.reminder as r WHERE t.user = ?2 and r.modifyDate > ?1")
	List<Reminder> findReminderSyncTask(Date lastUpdated, ApplicationUser user);
}
