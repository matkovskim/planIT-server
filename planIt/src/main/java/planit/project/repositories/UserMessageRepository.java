package planit.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import planit.project.model.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

}
