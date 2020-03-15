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

import mx.gob.scjn.desca.domain.Institution;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.InstitutionRepository;
import mx.gob.scjn.desca.repository.search.InstitutionSearchRepository;
import mx.gob.scjn.desca.service.dto.InstitutionCriteria;

import mx.gob.scjn.desca.service.dto.InstitutionDTO;
import mx.gob.scjn.desca.service.mapper.InstitutionMapper;

/**
 * Service for executing complex queries for Institution entities in the database.
 * The main input is a {@link InstitutionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InstitutionDTO} or a {@link Page} of {@link InstitutionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstitutionQueryService extends QueryService<Institution> {

    private final Logger log = LoggerFactory.getLogger(InstitutionQueryService.class);


    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper institutionMapper;

    private final InstitutionSearchRepository institutionSearchRepository;

    public InstitutionQueryService(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper, InstitutionSearchRepository institutionSearchRepository) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.institutionSearchRepository = institutionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InstitutionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InstitutionDTO> findByCriteria(InstitutionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Institution> specification = createSpecification(criteria);
        return institutionMapper.toDto(institutionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InstitutionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InstitutionDTO> findByCriteria(InstitutionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Institution> specification = createSpecification(criteria);
        final Page<Institution> result = institutionRepository.findAll(specification, page);
        return result.map(institutionMapper::toDto);
    }

    /**
     * Function to convert InstitutionCriteria to a {@link Specifications}
     */
    private Specifications<Institution> createSpecification(InstitutionCriteria criteria) {
        Specifications<Institution> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Institution_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Institution_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Institution_.enabled));
            }
        }
        return specification;
    }

}
