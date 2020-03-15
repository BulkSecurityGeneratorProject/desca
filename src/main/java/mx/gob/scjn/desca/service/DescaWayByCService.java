package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DescaWayByC.
 */
public interface DescaWayByCService {

    /**
     * Save a descaWayByC.
     *
     * @param descaWayByCDTO the entity to save
     * @return the persisted entity
     */
    DescaWayByCDTO save(DescaWayByCDTO descaWayByCDTO);

    /**
     * Get all the descaWayByCS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaWayByCDTO> findAll(Pageable pageable);

    /**
     * Get the "id" descaWayByC.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DescaWayByCDTO findOne(Long id);

    /**
     * Delete the "id" descaWayByC.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the descaWayByC corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaWayByCDTO> search(String query, Pageable pageable);
}
