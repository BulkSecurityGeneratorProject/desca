package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.ApplicantService;
import mx.gob.scjn.desca.domain.Applicant;
import mx.gob.scjn.desca.repository.ApplicantRepository;
import mx.gob.scjn.desca.repository.search.ApplicantSearchRepository;
import mx.gob.scjn.desca.service.dto.ApplicantDTO;
import mx.gob.scjn.desca.service.mapper.ApplicantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Applicant.
 */
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    private final ApplicantMapper applicantMapper;

    private final ApplicantSearchRepository applicantSearchRepository;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository, ApplicantMapper applicantMapper, ApplicantSearchRepository applicantSearchRepository) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
        this.applicantSearchRepository = applicantSearchRepository;
    }

    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicantDTO save(ApplicantDTO applicantDTO) {
        log.debug("Request to save Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        ApplicantDTO result = applicantMapper.toDto(applicant);
        applicantSearchRepository.save(applicant);
        return result;
    }

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable)
            .map(applicantMapper::toDto);
    }

    /**
     * Get one applicant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicantDTO findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        Applicant applicant = applicantRepository.findOne(id);
        return applicantMapper.toDto(applicant);
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
        applicantSearchRepository.delete(id);
    }

    /**
     * Search for the applicant corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Applicants for query {}", query);
        Page<Applicant> result = applicantSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(applicantMapper::toDto);
    }
}
