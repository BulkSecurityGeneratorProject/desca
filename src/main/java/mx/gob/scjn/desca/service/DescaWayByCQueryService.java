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

import mx.gob.scjn.desca.domain.DescaWayByC;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.DescaWayByCRepository;
import mx.gob.scjn.desca.repository.search.DescaWayByCSearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayByCCriteria;

import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayByCMapper;

/**
 * Service for executing complex queries for DescaWayByC entities in the database.
 * The main input is a {@link DescaWayByCCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DescaWayByCDTO} or a {@link Page} of {@link DescaWayByCDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DescaWayByCQueryService extends QueryService<DescaWayByC> {

    private final Logger log = LoggerFactory.getLogger(DescaWayByCQueryService.class);


    private final DescaWayByCRepository descaWayByCRepository;

    private final DescaWayByCMapper descaWayByCMapper;

    private final DescaWayByCSearchRepository descaWayByCSearchRepository;

    public DescaWayByCQueryService(DescaWayByCRepository descaWayByCRepository, DescaWayByCMapper descaWayByCMapper, DescaWayByCSearchRepository descaWayByCSearchRepository) {
        this.descaWayByCRepository = descaWayByCRepository;
        this.descaWayByCMapper = descaWayByCMapper;
        this.descaWayByCSearchRepository = descaWayByCSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DescaWayByCDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DescaWayByCDTO> findByCriteria(DescaWayByCCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DescaWayByC> specification = createSpecification(criteria);
        return descaWayByCMapper.toDto(descaWayByCRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DescaWayByCDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DescaWayByCDTO> findByCriteria(DescaWayByCCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DescaWayByC> specification = createSpecification(criteria);
        final Page<DescaWayByC> result = descaWayByCRepository.findAll(specification, page);
        return result.map(descaWayByCMapper::toDto);
    }

    /**
     * Function to convert DescaWayByCCriteria to a {@link Specifications}
     */
    private Specifications<DescaWayByC> createSpecification(DescaWayByCCriteria criteria) {
        Specifications<DescaWayByC> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DescaWayByC_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DescaWayByC_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), DescaWayByC_.enabled));
            }
        }
        return specification;
    }

}
