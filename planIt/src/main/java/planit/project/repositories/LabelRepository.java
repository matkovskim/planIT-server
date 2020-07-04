package planit.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import planit.project.model.ApplicationUser;
import planit.project.model.Label;
import planit.project.model.Reminder;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long>, JpaSpecificationExecutor<Label> {
	
	Label findByIdAndDeleted(Long id, boolean deleted);

	List<Label> findByModifyDateAfter(Date date);

	List<Label> findByDeleted(boolean deleted);
	
	@Query("SELECT l FROM TaskLabelConnection tc inner join tc.task as t inner join tc.label as l WHERE tc.deleted = ?1 and t.deleted = ?2 and l.deleted = ?4 and t.user = ?3")
	List<Label> findLabelFirstTime(boolean deletedConnection, boolean deletedTask, ApplicationUser user, boolean deletedLabel);
	
	@Query("SELECT l FROM TaskLabelConnection tc inner join tc.task as t inner join tc.label as l WHERE t.user = ?2 and l.modifyDate > ?1")
	List<Label> findLabelSync(Date lastUpdated, ApplicationUser user);
}
