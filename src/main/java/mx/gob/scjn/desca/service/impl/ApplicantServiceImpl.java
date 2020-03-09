package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.ApplicantService;
import mx.gob.scjn.desca.domain.Applicant;
import mx.gob.scjn.desca.repository.ApplicantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Applicant.
 */
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * Save a applicant.
     *
     * @param applicant the entity to save
     * @return the persisted entity
     */
    @Override
    public Applicant save(Applicant applicant) {
        log.debug("Request to save Applicant : {}", applicant);
        return applicantRepository.save(applicant);
    }

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Applicant> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable);
    }

    /**
     * Get one applicant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Applicant findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findOne(id);
    }

    /**
     * Delete the applicant by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.delete(id);
    }
}
