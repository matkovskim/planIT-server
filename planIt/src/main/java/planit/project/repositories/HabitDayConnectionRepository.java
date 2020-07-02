package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitDayConnection;

@Repository
public interface HabitDayConnectionRepository  extends JpaRepository<HabitDayConnection, Long>, JpaSpecificationExecutor<HabitDayConnection> {

	@Query("SELECT hc FROM HabitDayConnection hc inner join hc.habit as h WHERE hc.deleted = ?1 and h.deleted = ?2 and h.user = ?3")
	List<HabitDayConnection> findHabitDayConnectionFirstTime(boolean reminderConn, boolean deletedHabit, ApplicationUser user);
	
	@Query("SELECT hc FROM HabitDayConnection hc inner join hc.habit as h WHERE h.user = ?2 and hc.modifyDate > ?1")
	List<HabitDayConnection> findHabitDayConnectionSync(Date lastUpdated, ApplicationUser user);

}
