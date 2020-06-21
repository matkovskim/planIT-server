package planit.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import planit.project.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findByTitle(String title);

}
