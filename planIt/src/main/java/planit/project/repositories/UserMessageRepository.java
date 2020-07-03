package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import planit.project.model.ApplicationUser;
import planit.project.model.Team;
import planit.project.model.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

	List<UserMessage> findAllByTeam(Team team);

	@Query("SELECT mess FROM UserMessage mess, TeamUserConnection conn, Team t WHERE mess.team = t and conn.team = t and conn.user = ?1 and conn.deleted = ?2")
	List<UserMessage> findMessagesFirstSync(ApplicationUser user, boolean deleted);

	@Query("SELECT mess FROM UserMessage mess, TeamUserConnection conn, Team t WHERE mess.team = t and conn.team = t and conn.user = ?1 and mess.modifyDate > ?2")
	List<UserMessage> findMessagesSyncDate(ApplicationUser user, Date date);

}
