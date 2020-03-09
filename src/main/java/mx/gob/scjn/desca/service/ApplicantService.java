package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.domain.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Applicant.
 */
public interface ApplicantService {

    /**
     * Save a applicant.
     *
     * @param applicant the entity to save
     * @return the persisted entity
     */
    Applicant save(Applicant applicant);

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Applicant> findAll(Pageable pageable);

    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Applicant findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
