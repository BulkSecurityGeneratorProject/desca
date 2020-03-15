package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DescaWay.
 */
public interface DescaWayService {

    /**
     * Save a descaWay.
     *
     * @param descaWayDTO the entity to save
     * @return the persisted entity
     */
    DescaWayDTO save(DescaWayDTO descaWayDTO);

    /**
     * Get all the descaWays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaWayDTO> findAll(Pageable pageable);

    /**
     * Get the "id" descaWay.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DescaWayDTO findOne(Long id);

    /**
     * Delete the "id" descaWay.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the descaWay corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaWayDTO> search(String query, Pageable pageable);
}
