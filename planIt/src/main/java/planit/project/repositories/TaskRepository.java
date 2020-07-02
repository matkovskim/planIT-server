package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	
	Task  findByUserAndIdAndDeleted(ApplicationUser user, Long id, boolean deleted);

	List<Task> findByUserAndModifyDateAfter(ApplicationUser user , Date date);

	List<Task> findByUserAndDeleted(ApplicationUser user, boolean deleted );
	
	
}
