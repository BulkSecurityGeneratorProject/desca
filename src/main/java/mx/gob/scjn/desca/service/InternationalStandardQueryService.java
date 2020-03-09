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

import mx.gob.scjn.desca.domain.InternationalStandard;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.InternationalStandardRepository;
import mx.gob.scjn.desca.service.dto.InternationalStandardCriteria;

import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;
import mx.gob.scjn.desca.service.mapper.InternationalStandardMapper;

/**
 * Service for executing complex queries for InternationalStandard entities in the database.
 * The main input is a {@link InternationalStandardCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InternationalStandardDTO} or a {@link Page} of {@link InternationalStandardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InternationalStandardQueryService extends QueryService<InternationalStandard> {

    private final Logger log = LoggerFactory.getLogger(InternationalStandardQueryService.class);


    private final InternationalStandardRepository internationalStandardRepository;

    private final InternationalStandardMapper internationalStandardMapper;

    public InternationalStandardQueryService(InternationalStandardRepository internationalStandardRepository, InternationalStandardMapper internationalStandardMapper) {
        this.internationalStandardRepository = internationalStandardRepository;
        this.internationalStandardMapper = internationalStandardMapper;
    }

    /**
     * Return a {@link List} of {@link InternationalStandardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InternationalStandardDTO> findByCriteria(InternationalStandardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<InternationalStandard> specification = createSpecification(criteria);
        return internationalStandardMapper.toDto(internationalStandardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InternationalStandardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InternationalStandardDTO> findByCriteria(InternationalStandardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<InternationalStandard> specification = createSpecification(criteria);
        final Page<InternationalStandard> result = internationalStandardRepository.findAll(specification, page);
        return result.map(internationalStandardMapper::toDto);
    }

    /**
     * Function to convert InternationalStandardCriteria to a {@link Specifications}
     */
    private Specifications<InternationalStandard> createSpecification(InternationalStandardCriteria criteria) {
        Specifications<InternationalStandard> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InternationalStandard_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), InternationalStandard_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), InternationalStandard_.status));
            }
        }
        return specification;
    }

}
