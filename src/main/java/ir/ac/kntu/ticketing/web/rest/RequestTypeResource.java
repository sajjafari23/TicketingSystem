package ir.ac.kntu.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import ir.ac.kntu.ticketing.domain.RequestType;
import ir.ac.kntu.ticketing.service.RequestTypeService;
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
 * REST controller for managing RequestType.
 */
@RestController
@RequestMapping("/api")
public class RequestTypeResource {

    private final Logger log = LoggerFactory.getLogger(RequestTypeResource.class);

    private static final String ENTITY_NAME = "requestType";

    private final RequestTypeService requestTypeService;

    public RequestTypeResource(RequestTypeService requestTypeService) {
        this.requestTypeService = requestTypeService;
    }

    /**
     * POST  /request-types : Create a new requestType.
     *
     * @param requestType the requestType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requestType, or with status 400 (Bad Request) if the requestType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/request-types")
    @Timed
    public ResponseEntity<RequestType> createRequestType(@Valid @RequestBody RequestType requestType) throws URISyntaxException {
        log.debug("REST request to save RequestType : {}", requestType);
        if (requestType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new requestType cannot already have an ID")).body(null);
        }
        RequestType result = requestTypeService.save(requestType);
        return ResponseEntity.created(new URI("/api/request-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /request-types : Updates an existing requestType.
     *
     * @param requestType the requestType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requestType,
     * or with status 400 (Bad Request) if the requestType is not valid,
     * or with status 500 (Internal Server Error) if the requestType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/request-types")
    @Timed
    public ResponseEntity<RequestType> updateRequestType(@Valid @RequestBody RequestType requestType) throws URISyntaxException {
        log.debug("REST request to update RequestType : {}", requestType);
        if (requestType.getId() == null) {
            return createRequestType(requestType);
        }
        RequestType result = requestTypeService.save(requestType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requestType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /request-types : get all the requestTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requestTypes in body
     */
    @GetMapping("/request-types")
    @Timed
    public ResponseEntity<List<RequestType>> getAllRequestTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RequestTypes");
        Page<RequestType> page = requestTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/request-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /request-types/:id : get the "id" requestType.
     *
     * @param id the id of the requestType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requestType, or with status 404 (Not Found)
     */
    @GetMapping("/request-types/{id}")
    @Timed
    public ResponseEntity<RequestType> getRequestType(@PathVariable Long id) {
        log.debug("REST request to get RequestType : {}", id);
        RequestType requestType = requestTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(requestType));
    }

    /**
     * DELETE  /request-types/:id : delete the "id" requestType.
     *
     * @param id the id of the requestType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/request-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequestType(@PathVariable Long id) {
        log.debug("REST request to delete RequestType : {}", id);
        requestTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
