package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.VulnerableGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing VulnerableGroup.
 */
public interface VulnerableGroupService {

    /**
     * Save a vulnerableGroup.
     *
     * @param vulnerableGroup the entity to save
     * @return the persisted entity
     */
    VulnerableGroup save(VulnerableGroup vulnerableGroup);

    /**
     * Get all the vulnerableGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VulnerableGroup> findAll(Pageable pageable);

    /**
     * Get the "id" vulnerableGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VulnerableGroup findOne(Long id);

    /**
     * Delete the "id" vulnerableGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
