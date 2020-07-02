package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.HabitFulfillment;

@Repository
public interface HabitFulfillmentRepository extends JpaRepository<HabitFulfillment, Long>, JpaSpecificationExecutor<HabitFulfillment> {

	HabitFulfillment findByIdAndDeleted(Long id, boolean deleted);

	List<HabitFulfillment> findByModifyDateAfter(Date date);

	@Query("SELECT hf FROM HabitFulfillment hf inner join hf.habit as h WHERE hf.deleted = ?1 and h.deleted = ?2 and h.user = ?3")
	List<HabitFulfillment> findHabitFillmentsFirstTime(boolean deletedFulfillment, boolean deletedHabit, ApplicationUser user);
	
	@Query("SELECT hf FROM HabitFulfillment hf inner join hf.habit as h WHERE h.user = ?2 and hf.modifyDate > ?1")
	List<HabitFulfillment> findHabitFillmentsSync(Date lastUpdated, ApplicationUser user);
}
