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

import mx.gob.scjn.desca.domain.Methodology;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.MethodologyRepository;
import mx.gob.scjn.desca.repository.search.MethodologySearchRepository;
import mx.gob.scjn.desca.service.dto.MethodologyCriteria;

import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import mx.gob.scjn.desca.service.mapper.MethodologyMapper;

/**
 * Service for executing complex queries for Methodology entities in the database.
 * The main input is a {@link MethodologyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MethodologyDTO} or a {@link Page} of {@link MethodologyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MethodologyQueryService extends QueryService<Methodology> {

    private final Logger log = LoggerFactory.getLogger(MethodologyQueryService.class);


    private final MethodologyRepository methodologyRepository;

    private final MethodologyMapper methodologyMapper;

    private final MethodologySearchRepository methodologySearchRepository;

    public MethodologyQueryService(MethodologyRepository methodologyRepository, MethodologyMapper methodologyMapper, MethodologySearchRepository methodologySearchRepository) {
        this.methodologyRepository = methodologyRepository;
        this.methodologyMapper = methodologyMapper;
        this.methodologySearchRepository = methodologySearchRepository;
    }

    /**
     * Return a {@link List} of {@link MethodologyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MethodologyDTO> findByCriteria(MethodologyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Methodology> specification = createSpecification(criteria);
        return methodologyMapper.toDto(methodologyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MethodologyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MethodologyDTO> findByCriteria(MethodologyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Methodology> specification = createSpecification(criteria);
        final Page<Methodology> result = methodologyRepository.findAll(specification, page);
        return result.map(methodologyMapper::toDto);
    }

    /**
     * Function to convert MethodologyCriteria to a {@link Specifications}
     */
    private Specifications<Methodology> createSpecification(MethodologyCriteria criteria) {
        Specifications<Methodology> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Methodology_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Methodology_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Methodology_.status));
            }
        }
        return specification;
    }

}
