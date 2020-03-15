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

import mx.gob.scjn.desca.domain.DescaWay;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.DescaWayRepository;
import mx.gob.scjn.desca.repository.search.DescaWaySearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayCriteria;

import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayMapper;

/**
 * Service for executing complex queries for DescaWay entities in the database.
 * The main input is a {@link DescaWayCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DescaWayDTO} or a {@link Page} of {@link DescaWayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DescaWayQueryService extends QueryService<DescaWay> {

    private final Logger log = LoggerFactory.getLogger(DescaWayQueryService.class);


    private final DescaWayRepository descaWayRepository;

    private final DescaWayMapper descaWayMapper;

    private final DescaWaySearchRepository descaWaySearchRepository;

    public DescaWayQueryService(DescaWayRepository descaWayRepository, DescaWayMapper descaWayMapper, DescaWaySearchRepository descaWaySearchRepository) {
        this.descaWayRepository = descaWayRepository;
        this.descaWayMapper = descaWayMapper;
        this.descaWaySearchRepository = descaWaySearchRepository;
    }

    /**
     * Return a {@link List} of {@link DescaWayDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DescaWayDTO> findByCriteria(DescaWayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DescaWay> specification = createSpecification(criteria);
        return descaWayMapper.toDto(descaWayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DescaWayDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DescaWayDTO> findByCriteria(DescaWayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DescaWay> specification = createSpecification(criteria);
        final Page<DescaWay> result = descaWayRepository.findAll(specification, page);
        return result.map(descaWayMapper::toDto);
    }

    /**
     * Function to convert DescaWayCriteria to a {@link Specifications}
     */
    private Specifications<DescaWay> createSpecification(DescaWayCriteria criteria) {
        Specifications<DescaWay> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DescaWay_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DescaWay_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DescaWay_.status));
            }
        }
        return specification;
    }

}
