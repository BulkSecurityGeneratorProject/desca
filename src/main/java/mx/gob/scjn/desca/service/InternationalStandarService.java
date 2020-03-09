package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.InternationalStandar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing InternationalStandar.
 */
public interface InternationalStandarService {

    /**
     * Save a internationalStandar.
     *
     * @param internationalStandar the entity to save
     * @return the persisted entity
     */
    InternationalStandar save(InternationalStandar internationalStandar);

    /**
     * Get all the internationalStandars.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InternationalStandar> findAll(Pageable pageable);

    /**
     * Get the "id" internationalStandar.
     *
     * @param id the id of the entity
     * @return the entity
     */
    InternationalStandar findOne(Long id);

    /**
     * Delete the "id" internationalStandar.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
