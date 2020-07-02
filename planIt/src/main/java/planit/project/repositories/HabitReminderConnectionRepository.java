package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitReminderConnection;

@Repository
public interface HabitReminderConnectionRepository extends JpaRepository<HabitReminderConnection, Long>, JpaSpecificationExecutor<HabitReminderConnection> {

	@Query("SELECT hc FROM HabitReminderConnection hc inner join hc.habit as h WHERE hc.deleted = ?1 and h.deleted = ?2 and h.user = ?3")
	List<HabitReminderConnection> findHabitReminderConnectionsFirstTime(boolean reminderConn, boolean deletedHabit, ApplicationUser user);
	
	@Query("SELECT hc FROM HabitReminderConnection hc inner join hc.habit as h WHERE h.user = ?2 and hc.modifyDate > ?1")
	List<HabitReminderConnection> findHabitReminderConnectionsSync(Date lastUpdated, ApplicationUser user);
}
