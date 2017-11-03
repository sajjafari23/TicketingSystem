package ir.ac.kntu.ticketing.repository;

import ir.ac.kntu.ticketing.domain.TaskType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

}
