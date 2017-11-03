package ir.ac.kntu.ticketing.service.impl;

import ir.ac.kntu.ticketing.service.RequestTypeService;
import ir.ac.kntu.ticketing.domain.RequestType;
import ir.ac.kntu.ticketing.repository.RequestTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing RequestType.
 */
@Service
@Transactional
public class RequestTypeServiceImpl implements RequestTypeService{

    private final Logger log = LoggerFactory.getLogger(RequestTypeServiceImpl.class);

    private final RequestTypeRepository requestTypeRepository;

    public RequestTypeServiceImpl(RequestTypeRepository requestTypeRepository) {
        this.requestTypeRepository = requestTypeRepository;
    }

    /**
     * Save a requestType.
     *
     * @param requestType the entity to save
     * @return the persisted entity
     */
    @Override
    public RequestType save(RequestType requestType) {
        log.debug("Request to save RequestType : {}", requestType);
        return requestTypeRepository.save(requestType);
    }

    /**
     *  Get all the requestTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestType> findAll(Pageable pageable) {
        log.debug("Request to get all RequestTypes");
        return requestTypeRepository.findAll(pageable);
    }

    /**
     *  Get one requestType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RequestType findOne(Long id) {
        log.debug("Request to get RequestType : {}", id);
        return requestTypeRepository.findOne(id);
    }

    /**
     *  Delete the  requestType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RequestType : {}", id);
        requestTypeRepository.delete(id);
    }
}
