package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Task;
import planit.project.model.TaskLabelConnection;

@Repository
public interface TaskLabelConnectionRepository extends JpaRepository<TaskLabelConnection, Long>, JpaSpecificationExecutor<TaskLabelConnection> {

	@Query("SELECT tc FROM TaskLabelConnection tc inner join tc.task as t inner join tc.label as l WHERE tc.deleted = ?1 and t.deleted = ?2 and l.deleted = ?4 and t.user = ?3")
	List<TaskLabelConnection> findTaskLabelConnectionFirstTime(boolean reminderConn, boolean taskDeleted, ApplicationUser user, boolean deleted);
	
	@Query("SELECT tc FROM TaskLabelConnection tc inner join tc.task as t WHERE t.user = ?2 and tc.modifyDate > ?1")
	List<TaskLabelConnection> findTaskLabelConnectionSync(Date lastUpdated, ApplicationUser user);
	
	List<TaskLabelConnection> findByTask(Task task);
	
	List<TaskLabelConnection> findByTaskAndDeleted(Task task, Boolean deleted);

}
