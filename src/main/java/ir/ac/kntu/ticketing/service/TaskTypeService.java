package ir.ac.kntu.ticketing.service;

import ir.ac.kntu.ticketing.domain.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TaskType.
 */
public interface TaskTypeService {

    /**
     * Save a taskType.
     *
     * @param taskType the entity to save
     * @return the persisted entity
     */
    TaskType save(TaskType taskType);

    /**
     *  Get all the taskTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskType> findAll(Pageable pageable);

    /**
     *  Get the "id" taskType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskType findOne(Long id);

    /**
     *  Delete the "id" taskType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
