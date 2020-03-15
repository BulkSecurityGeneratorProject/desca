package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.MemberStateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MemberState.
 */
public interface MemberStateService {

    /**
     * Save a memberState.
     *
     * @param memberStateDTO the entity to save
     * @return the persisted entity
     */
    MemberStateDTO save(MemberStateDTO memberStateDTO);

    /**
     * Get all the memberStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MemberStateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" memberState.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MemberStateDTO findOne(Long id);

    /**
     * Delete the "id" memberState.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the memberState corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MemberStateDTO> search(String query, Pageable pageable);
}
