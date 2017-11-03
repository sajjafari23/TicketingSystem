package ir.ac.kntu.ticketing.service;

import ir.ac.kntu.ticketing.domain.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing RequestType.
 */
public interface RequestTypeService {

    /**
     * Save a requestType.
     *
     * @param requestType the entity to save
     * @return the persisted entity
     */
    RequestType save(RequestType requestType);

    /**
     *  Get all the requestTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RequestType> findAll(Pageable pageable);

    /**
     *  Get the "id" requestType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RequestType findOne(Long id);

    /**
     *  Delete the "id" requestType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
