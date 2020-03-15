package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.DescaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Desca.
 */
public interface DescaService {

    /**
     * Save a desca.
     *
     * @param descaDTO the entity to save
     * @return the persisted entity
     */
    DescaDTO save(DescaDTO descaDTO);

    /**
     * Get all the descas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" desca.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DescaDTO findOne(Long id);

    /**
     * Delete the "id" desca.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the desca corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DescaDTO> search(String query, Pageable pageable);
}
