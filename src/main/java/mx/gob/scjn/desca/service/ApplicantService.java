package mx.gob.scjn.desca.service;

import mx.gob.scjn.desca.service.dto.ApplicantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Applicant.
 */
public interface ApplicantService {

    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save
     * @return the persisted entity
     */
    ApplicantDTO save(ApplicantDTO applicantDTO);

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ApplicantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" applicant.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicantDTO findOne(Long id);

    /**
     * Delete the "id" applicant.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the applicant corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ApplicantDTO> search(String query, Pageable pageable);
}
