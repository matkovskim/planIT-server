package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.TeamUserConnection;

@Repository
public interface TeamUserConnectionRepository extends JpaRepository<TeamUserConnection, Long>, JpaSpecificationExecutor<TeamUserConnection> {

	@Query("SELECT conn FROM TeamUserConnection conn WHERE conn.user = ?1 and conn.team = ?2 and conn.deleted = ?3 ")
	TeamUserConnection findIsMember(ApplicationUser user, Team team, boolean deleted);
	
	@Query("SELECT conn FROM TeamUserConnection conn WHERE conn.team = ?1 and conn.deleted = ?2 ")
	List<TeamUserConnection> findMembers(Team team, boolean deleted);
	
	@Query("SELECT conn FROM TeamUserConnection conn WHERE conn.team = ?1 and conn.modifyDate > ?2")
	List<TeamUserConnection> findModifiedMembers(Team team, Date date);
	
	@Query("SELECT u FROM TeamUserConnection conn inner join conn.user as u WHERE conn.team = ?1 and conn.deleted = ?2 ")
	List<ApplicationUser> findUserMembers(Team team, boolean deleted);
	
	@Query("SELECT t FROM TeamUserConnection conn inner join conn.team as t WHERE conn.user = ?1 and conn.deleted = ?2 and t.deleted = ?3 ")
	List<Team> findUserTeamsConnFirstSync(ApplicationUser user, boolean deletedConn, boolean deletedTeam);
	
	@Query("SELECT t FROM TeamUserConnection conn inner join conn.team as t WHERE conn.user = ?1 and ((conn.deleted = ?2 and conn.modifyDate > ?3) or conn.deleted = ?4 ) ")
	List<Team> findUserTeamsConnTeam(ApplicationUser user, boolean connDeleted1, Date date, boolean connDeleted2);
	
	@Query("SELECT conn FROM TeamUserConnection conn inner join conn.team as t WHERE conn.user = ?1 and conn.modifyDate > ?2")
	List<TeamUserConnection> findUserTeamsConnDate(ApplicationUser user, Date date);
}
