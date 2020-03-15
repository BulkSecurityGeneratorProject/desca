package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MainDatabase.
 */
public interface MainDatabaseService {

    /**
     * Save a mainDatabase.
     *
     * @param mainDatabaseDTO the entity to save
     * @return the persisted entity
     */
    MainDatabaseDTO save(MainDatabaseDTO mainDatabaseDTO);

    /**
     * Get all the mainDatabases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MainDatabaseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mainDatabase.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MainDatabaseDTO findOne(Long id);

    /**
     * Delete the "id" mainDatabase.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the mainDatabase corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MainDatabaseDTO> search(String query, Pageable pageable);
}
