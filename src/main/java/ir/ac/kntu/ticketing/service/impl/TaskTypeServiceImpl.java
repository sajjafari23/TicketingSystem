package ir.ac.kntu.ticketing.service.impl;

import ir.ac.kntu.ticketing.service.TaskTypeService;
import ir.ac.kntu.ticketing.domain.TaskType;
import ir.ac.kntu.ticketing.repository.TaskTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TaskType.
 */
@Service
@Transactional
public class TaskTypeServiceImpl implements TaskTypeService{

    private final Logger log = LoggerFactory.getLogger(TaskTypeServiceImpl.class);

    private final TaskTypeRepository taskTypeRepository;

    public TaskTypeServiceImpl(TaskTypeRepository taskTypeRepository) {
        this.taskTypeRepository = taskTypeRepository;
    }

    /**
     * Save a taskType.
     *
     * @param taskType the entity to save
     * @return the persisted entity
     */
    @Override
    public TaskType save(TaskType taskType) {
        log.debug("Request to save TaskType : {}", taskType);
        return taskTypeRepository.save(taskType);
    }

    /**
     *  Get all the taskTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskType> findAll(Pageable pageable) {
        log.debug("Request to get all TaskTypes");
        return taskTypeRepository.findAll(pageable);
    }

    /**
     *  Get one taskType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TaskType findOne(Long id) {
        log.debug("Request to get TaskType : {}", id);
        return taskTypeRepository.findOne(id);
    }

    /**
     *  Delete the  taskType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskType : {}", id);
        taskTypeRepository.delete(id);
    }
}
