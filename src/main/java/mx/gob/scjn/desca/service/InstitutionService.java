package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.InstitutionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Institution.
 */
public interface InstitutionService {

    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save
     * @return the persisted entity
     */
    InstitutionDTO save(InstitutionDTO institutionDTO);

    /**
     * Get all the institutions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InstitutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" institution.
     *
     * @param id the id of the entity
     * @return the entity
     */
    InstitutionDTO findOne(Long id);

    /**
     * Delete the "id" institution.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the institution corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InstitutionDTO> search(String query, Pageable pageable);
}
