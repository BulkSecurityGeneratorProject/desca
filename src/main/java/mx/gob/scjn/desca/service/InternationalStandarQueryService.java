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

import mx.gob.scjn.desca.domain.InternationalStandar;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.InternationalStandarRepository;
import mx.gob.scjn.desca.service.dto.InternationalStandarCriteria;


/**
 * Service for executing complex queries for InternationalStandar entities in the database.
 * The main input is a {@link InternationalStandarCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InternationalStandar} or a {@link Page} of {@link InternationalStandar} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InternationalStandarQueryService extends QueryService<InternationalStandar> {

    private final Logger log = LoggerFactory.getLogger(InternationalStandarQueryService.class);


    private final InternationalStandarRepository internationalStandarRepository;

    public InternationalStandarQueryService(InternationalStandarRepository internationalStandarRepository) {
        this.internationalStandarRepository = internationalStandarRepository;
    }

    /**
     * Return a {@link List} of {@link InternationalStandar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InternationalStandar> findByCriteria(InternationalStandarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<InternationalStandar> specification = createSpecification(criteria);
        return internationalStandarRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InternationalStandar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InternationalStandar> findByCriteria(InternationalStandarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<InternationalStandar> specification = createSpecification(criteria);
        return internationalStandarRepository.findAll(specification, page);
    }

    /**
     * Function to convert InternationalStandarCriteria to a {@link Specifications}
     */
    private Specifications<InternationalStandar> createSpecification(InternationalStandarCriteria criteria) {
        Specifications<InternationalStandar> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InternationalStandar_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), InternationalStandar_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), InternationalStandar_.status));
            }
        }
        return specification;
    }

}
