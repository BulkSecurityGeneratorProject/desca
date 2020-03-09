package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing InternationalStandard.
 */
public interface InternationalStandardService {

    /**
     * Save a internationalStandard.
     *
     * @param internationalStandardDTO the entity to save
     * @return the persisted entity
     */
    InternationalStandardDTO save(InternationalStandardDTO internationalStandardDTO);

    /**
     * Get all the internationalStandards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InternationalStandardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" internationalStandard.
     *
     * @param id the id of the entity
     * @return the entity
     */
    InternationalStandardDTO findOne(Long id);

    /**
     * Delete the "id" internationalStandard.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
