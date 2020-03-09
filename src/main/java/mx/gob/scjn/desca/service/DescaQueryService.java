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

import mx.gob.scjn.desca.domain.Desca;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.DescaRepository;
import mx.gob.scjn.desca.service.dto.DescaCriteria;

import mx.gob.scjn.desca.service.dto.DescaDTO;
import mx.gob.scjn.desca.service.mapper.DescaMapper;

/**
 * Service for executing complex queries for Desca entities in the database.
 * The main input is a {@link DescaCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DescaDTO} or a {@link Page} of {@link DescaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DescaQueryService extends QueryService<Desca> {

    private final Logger log = LoggerFactory.getLogger(DescaQueryService.class);


    private final DescaRepository descaRepository;

    private final DescaMapper descaMapper;

    public DescaQueryService(DescaRepository descaRepository, DescaMapper descaMapper) {
        this.descaRepository = descaRepository;
        this.descaMapper = descaMapper;
    }

    /**
     * Return a {@link List} of {@link DescaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DescaDTO> findByCriteria(DescaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Desca> specification = createSpecification(criteria);
        return descaMapper.toDto(descaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DescaDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DescaDTO> findByCriteria(DescaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Desca> specification = createSpecification(criteria);
        final Page<Desca> result = descaRepository.findAll(specification, page);
        return result.map(descaMapper::toDto);
    }

    /**
     * Function to convert DescaCriteria to a {@link Specifications}
     */
    private Specifications<Desca> createSpecification(DescaCriteria criteria) {
        Specifications<Desca> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Desca_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Desca_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Desca_.status));
            }
        }
        return specification;
    }

}
