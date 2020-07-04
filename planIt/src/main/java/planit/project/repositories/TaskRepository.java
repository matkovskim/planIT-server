package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Task;
import planit.project.model.Team;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	
	Task findByUserAndIdAndDeleted(ApplicationUser user, Long id, boolean deleted);

	List<Task> findByUserAndModifyDateAfter(ApplicationUser user , Date date);

	List<Task> findByUserAndDeleted(ApplicationUser user, boolean deleted );
	
	Task findByIdAndDeleted(Long id, boolean deleted);
	
	@Query("SELECT t FROM Task t, Team team, TeamUserConnection tc WHERE tc.team = team and t.team = team and tc.user = ?1  and tc.deleted = ?3 and t.deleted = ?2 ")
	List<Task> findAllTeamTasks(ApplicationUser user, boolean deleted, boolean deletedConn );
	
	@Query("SELECT t FROM Task t, Team team, TeamUserConnection tc WHERE tc.team = team and t.team = team and tc.user = ?1 and tc.modifyDate > ?2 ")
	List<Task> findAllSyncTeamTasks(ApplicationUser user, Date lastUpdated );
	
	List<Task> findByTeam(Team team);

}
