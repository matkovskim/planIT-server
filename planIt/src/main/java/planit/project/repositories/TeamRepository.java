package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import planit.project.model.ApplicationUser;
import planit.project.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findByTitle(String title);

	Team findByIdAndDeleted(Long id, boolean deleted);

	List<Team> findByModifyDateAfter(Date date);

	List<Team> findByDeleted(boolean deleted);

	@Query("SELECT t FROM TeamUserConnection conn inner join conn.team as t WHERE conn.user = ?1 and conn.deleted = ?2 and t.deleted = ?3 ")
	List<Team> findUserTeamsFirstSync(ApplicationUser user, boolean deletedConn, boolean deletedTeam);

	@Query("SELECT t FROM TeamUserConnection conn inner join conn.team as t WHERE conn.user = ?1 and t.modifyDate > ?2")
	List<Team> findUserTeamsSyncDate(ApplicationUser user, Date date);

}
