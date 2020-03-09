package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.VulnerableGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing VulnerableGroup.
 */
public interface VulnerableGroupService {

    /**
     * Save a vulnerableGroup.
     *
     * @param vulnerableGroupDTO the entity to save
     * @return the persisted entity
     */
    VulnerableGroupDTO save(VulnerableGroupDTO vulnerableGroupDTO);

    /**
     * Get all the vulnerableGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VulnerableGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vulnerableGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VulnerableGroupDTO findOne(Long id);

    /**
     * Delete the "id" vulnerableGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
