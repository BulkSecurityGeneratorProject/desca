package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing JudicialProcessType.
 */
public interface JudicialProcessTypeService {

    /**
     * Save a judicialProcessType.
     *
     * @param judicialProcessTypeDTO the entity to save
     * @return the persisted entity
     */
    JudicialProcessTypeDTO save(JudicialProcessTypeDTO judicialProcessTypeDTO);

    /**
     * Get all the judicialProcessTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<JudicialProcessTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" judicialProcessType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    JudicialProcessTypeDTO findOne(Long id);

    /**
     * Delete the "id" judicialProcessType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the judicialProcessType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<JudicialProcessTypeDTO> search(String query, Pageable pageable);
}
