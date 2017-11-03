package ir.ac.kntu.ticketing.repository;

import ir.ac.kntu.ticketing.domain.Task;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where task.user.login = ?#{principal.username}")
    List<Task> findByUserIsCurrentUser();

}
