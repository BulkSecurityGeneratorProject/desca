package mx.gob.scjn.desca.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mx.gob.scjn.desca.domain.Applicant;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.ApplicantRepository;
import mx.gob.scjn.desca.service.dto.ApplicantCriteria;


/**
 * Service for executing complex queries for Applicant entities in the database.
 * The main input is a {@link ApplicantCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Applicant} or a {@link Page} of {@link Applicant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicantQueryService extends QueryService<Applicant> {

    private final Logger log = LoggerFactory.getLogger(ApplicantQueryService.class);


    private final ApplicantRepository applicantRepository;

    public ApplicantQueryService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * Return a {@link List} of {@link Applicant} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Applicant> findByCriteria(ApplicantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Applicant> specification = createSpecification(criteria);
        return applicantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Applicant} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Applicant> findByCriteria(ApplicantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Applicant> specification = createSpecification(criteria);
        return applicantRepository.findAll(specification, page);
    }

    /**
     * Function to convert ApplicantCriteria to a {@link Specifications}
     */
    private Specifications<Applicant> createSpecification(ApplicantCriteria criteria) {
        Specifications<Applicant> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Applicant_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Applicant_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Applicant_.status));
            }
        }
        return specification;
    }

}
