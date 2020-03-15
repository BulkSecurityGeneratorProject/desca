package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.RepairDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Repair.
 */
public interface RepairService {

    /**
     * Save a repair.
     *
     * @param repairDTO the entity to save
     * @return the persisted entity
     */
    RepairDTO save(RepairDTO repairDTO);

    /**
     * Get all the repairs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RepairDTO> findAll(Pageable pageable);

    /**
     * Get the "id" repair.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RepairDTO findOne(Long id);

    /**
     * Delete the "id" repair.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the repair corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RepairDTO> search(String query, Pageable pageable);
}
