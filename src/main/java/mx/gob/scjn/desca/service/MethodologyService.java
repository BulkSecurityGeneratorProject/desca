package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Methodology.
 */
public interface MethodologyService {

    /**
     * Save a methodology.
     *
     * @param methodologyDTO the entity to save
     * @return the persisted entity
     */
    MethodologyDTO save(MethodologyDTO methodologyDTO);

    /**
     * Get all the methodologies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MethodologyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" methodology.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MethodologyDTO findOne(Long id);

    /**
     * Delete the "id" methodology.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the methodology corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MethodologyDTO> search(String query, Pageable pageable);
}
