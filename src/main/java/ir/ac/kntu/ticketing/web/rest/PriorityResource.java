package ir.ac.kntu.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import ir.ac.kntu.ticketing.domain.Priority;
import ir.ac.kntu.ticketing.service.PriorityService;
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
 * REST controller for managing Priority.
 */
@RestController
@RequestMapping("/api")
public class PriorityResource {

    private final Logger log = LoggerFactory.getLogger(PriorityResource.class);

    private static final String ENTITY_NAME = "priority";

    private final PriorityService priorityService;

    public PriorityResource(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    /**
     * POST  /priorities : Create a new priority.
     *
     * @param priority the priority to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priority, or with status 400 (Bad Request) if the priority has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/priorities")
    @Timed
    public ResponseEntity<Priority> createPriority(@Valid @RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to save Priority : {}", priority);
        if (priority.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new priority cannot already have an ID")).body(null);
        }
        Priority result = priorityService.save(priority);
        return ResponseEntity.created(new URI("/api/priorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /priorities : Updates an existing priority.
     *
     * @param priority the priority to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priority,
     * or with status 400 (Bad Request) if the priority is not valid,
     * or with status 500 (Internal Server Error) if the priority couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/priorities")
    @Timed
    public ResponseEntity<Priority> updatePriority(@Valid @RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to update Priority : {}", priority);
        if (priority.getId() == null) {
            return createPriority(priority);
        }
        Priority result = priorityService.save(priority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, priority.getId().toString()))
            .body(result);
    }

    /**
     * GET  /priorities : get all the priorities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of priorities in body
     */
    @GetMapping("/priorities")
    @Timed
    public ResponseEntity<List<Priority>> getAllPriorities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Priorities");
        Page<Priority> page = priorityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/priorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /priorities/:id : get the "id" priority.
     *
     * @param id the id of the priority to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priority, or with status 404 (Not Found)
     */
    @GetMapping("/priorities/{id}")
    @Timed
    public ResponseEntity<Priority> getPriority(@PathVariable Long id) {
        log.debug("REST request to get Priority : {}", id);
        Priority priority = priorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(priority));
    }

    /**
     * DELETE  /priorities/:id : delete the "id" priority.
     *
     * @param id the id of the priority to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/priorities/{id}")
    @Timed
    public ResponseEntity<Void> deletePriority(@PathVariable Long id) {
        log.debug("REST request to delete Priority : {}", id);
        priorityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
