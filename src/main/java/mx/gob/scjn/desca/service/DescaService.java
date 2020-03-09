package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.Desca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Desca.
 */
public interface DescaService {

    /**
     * Save a desca.
     *
     * @param desca the entity to save
     * @return the persisted entity
     */
    Desca save(Desca desca);

    /**
     * Get all the descas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Desca> findAll(Pageable pageable);

    /**
     * Get the "id" desca.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Desca findOne(Long id);

    /**
     * Delete the "id" desca.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
