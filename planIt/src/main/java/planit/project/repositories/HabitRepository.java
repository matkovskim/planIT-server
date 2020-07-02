package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long>, JpaSpecificationExecutor<Habit> {
	
	Habit findByUserAndIdAndDeleted(ApplicationUser user, Long id, boolean deleted);
	
	List<Habit> findByUserAndModifyDateAfter(ApplicationUser user, Date date);
	
	List<Habit> findByUserAndDeleted(ApplicationUser user,boolean deleted);

}
