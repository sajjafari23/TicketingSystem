package ir.ac.kntu.ticketing.service.impl;

import ir.ac.kntu.ticketing.service.PriorityService;
import ir.ac.kntu.ticketing.domain.Priority;
import ir.ac.kntu.ticketing.repository.PriorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Priority.
 */
@Service
@Transactional
public class PriorityServiceImpl implements PriorityService{

    private final Logger log = LoggerFactory.getLogger(PriorityServiceImpl.class);

    private final PriorityRepository priorityRepository;

    public PriorityServiceImpl(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    /**
     * Save a priority.
     *
     * @param priority the entity to save
     * @return the persisted entity
     */
    @Override
    public Priority save(Priority priority) {
        log.debug("Request to save Priority : {}", priority);
        return priorityRepository.save(priority);
    }

    /**
     *  Get all the priorities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Priority> findAll(Pageable pageable) {
        log.debug("Request to get all Priorities");
        return priorityRepository.findAll(pageable);
    }

    /**
     *  Get one priority by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Priority findOne(Long id) {
        log.debug("Request to get Priority : {}", id);
        return priorityRepository.findOne(id);
    }

    /**
     *  Delete the  priority by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Priority : {}", id);
        priorityRepository.delete(id);
    }
}
