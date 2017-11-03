package ir.ac.kntu.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import ir.ac.kntu.ticketing.domain.TaskType;
import ir.ac.kntu.ticketing.service.TaskTypeService;
import ir.ac.kntu.ticketing.web.rest.util.HeaderUtil;
import ir.ac.kntu.ticketing.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaskType.
 */
@RestController
@RequestMapping("/api")
public class TaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTypeResource.class);

    private static final String ENTITY_NAME = "taskType";

    private final TaskTypeService taskTypeService;

    public TaskTypeResource(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    /**
     * POST  /task-types : Create a new taskType.
     *
     * @param taskType the taskType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskType, or with status 400 (Bad Request) if the taskType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-types")
    @Timed
    public ResponseEntity<TaskType> createTaskType(@Valid @RequestBody TaskType taskType) throws URISyntaxException {
        log.debug("REST request to save TaskType : {}", taskType);
        if (taskType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new taskType cannot already have an ID")).body(null);
        }
        TaskType result = taskTypeService.save(taskType);
        return ResponseEntity.created(new URI("/api/task-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-types : Updates an existing taskType.
     *
     * @param taskType the taskType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskType,
     * or with status 400 (Bad Request) if the taskType is not valid,
     * or with status 500 (Internal Server Error) if the taskType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-types")
    @Timed
    public ResponseEntity<TaskType> updateTaskType(@Valid @RequestBody TaskType taskType) throws URISyntaxException {
        log.debug("REST request to update TaskType : {}", taskType);
        if (taskType.getId() == null) {
            return createTaskType(taskType);
        }
        TaskType result = taskTypeService.save(taskType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-types : get all the taskTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taskTypes in body
     */
    @GetMapping("/task-types")
    @Timed
    public ResponseEntity<List<TaskType>> getAllTaskTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TaskTypes");
        Page<TaskType> page = taskTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-types/:id : get the "id" taskType.
     *
     * @param id the id of the taskType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskType, or with status 404 (Not Found)
     */
    @GetMapping("/task-types/{id}")
    @Timed
    public ResponseEntity<TaskType> getTaskType(@PathVariable Long id) {
        log.debug("REST request to get TaskType : {}", id);
        TaskType taskType = taskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskType));
    }

    /**
     * DELETE  /task-types/:id : delete the "id" taskType.
     *
     * @param id the id of the taskType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskType(@PathVariable Long id) {
        log.debug("REST request to delete TaskType : {}", id);
        taskTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
