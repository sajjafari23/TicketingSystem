package ir.ac.kntu.ticketing.service;

import ir.ac.kntu.ticketing.domain.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Priority.
 */
public interface PriorityService {

    /**
     * Save a priority.
     *
     * @param priority the entity to save
     * @return the persisted entity
     */
    Priority save(Priority priority);

    /**
     *  Get all the priorities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Priority> findAll(Pageable pageable);

    /**
     *  Get the "id" priority.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Priority findOne(Long id);

    /**
     *  Delete the "id" priority.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
