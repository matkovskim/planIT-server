package planit.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import planit.project.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

	ApplicationUser findByEmail(String email);

}
