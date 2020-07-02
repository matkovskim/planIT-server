package planit.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import planit.project.model.Team;
import planit.project.model.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

	List<UserMessage> findAllByTeam(Team team);

}
