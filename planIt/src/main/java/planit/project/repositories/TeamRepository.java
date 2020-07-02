package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import planit.project.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findByTitle(String title);
	
	Team findByIdAndDeleted(Long id, boolean deleted);

	List<Team> findByModifyDateAfter(Date date);

	List<Team> findByDeleted(boolean deleted);
	
	

}
