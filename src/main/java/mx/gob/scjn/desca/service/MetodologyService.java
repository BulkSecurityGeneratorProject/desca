package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.Metodology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Metodology.
 */
public interface MetodologyService {

    /**
     * Save a metodology.
     *
     * @param metodology the entity to save
     * @return the persisted entity
     */
    Metodology save(Metodology metodology);

    /**
     * Get all the metodologies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Metodology> findAll(Pageable pageable);

    /**
     * Get the "id" metodology.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Metodology findOne(Long id);

    /**
     * Delete the "id" metodology.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
