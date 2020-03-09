package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.MemberState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MemberState.
 */
public interface MemberStateService {

    /**
     * Save a memberState.
     *
     * @param memberState the entity to save
     * @return the persisted entity
     */
    MemberState save(MemberState memberState);

    /**
     * Get all the memberStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MemberState> findAll(Pageable pageable);

    /**
     * Get the "id" memberState.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MemberState findOne(Long id);

    /**
     * Delete the "id" memberState.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
