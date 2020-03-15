package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.RepairTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing RepairType.
 */
public interface RepairTypeService {

    /**
     * Save a repairType.
     *
     * @param repairTypeDTO the entity to save
     * @return the persisted entity
     */
    RepairTypeDTO save(RepairTypeDTO repairTypeDTO);

    /**
     * Get all the repairTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RepairTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" repairType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RepairTypeDTO findOne(Long id);

    /**
     * Delete the "id" repairType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the repairType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RepairTypeDTO> search(String query, Pageable pageable);
}
