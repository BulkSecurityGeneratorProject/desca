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

import mx.gob.scjn.desca.domain.Repair;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.RepairRepository;
import mx.gob.scjn.desca.repository.search.RepairSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairCriteria;

import mx.gob.scjn.desca.service.dto.RepairDTO;
import mx.gob.scjn.desca.service.mapper.RepairMapper;

/**
 * Service for executing complex queries for Repair entities in the database.
 * The main input is a {@link RepairCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RepairDTO} or a {@link Page} of {@link RepairDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairQueryService extends QueryService<Repair> {

    private final Logger log = LoggerFactory.getLogger(RepairQueryService.class);


    private final RepairRepository repairRepository;

    private final RepairMapper repairMapper;

    private final RepairSearchRepository repairSearchRepository;

    public RepairQueryService(RepairRepository repairRepository, RepairMapper repairMapper, RepairSearchRepository repairSearchRepository) {
        this.repairRepository = repairRepository;
        this.repairMapper = repairMapper;
        this.repairSearchRepository = repairSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RepairDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RepairDTO> findByCriteria(RepairCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Repair> specification = createSpecification(criteria);
        return repairMapper.toDto(repairRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RepairDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairDTO> findByCriteria(RepairCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Repair> specification = createSpecification(criteria);
        final Page<Repair> result = repairRepository.findAll(specification, page);
        return result.map(repairMapper::toDto);
    }

    /**
     * Function to convert RepairCriteria to a {@link Specifications}
     */
    private Specifications<Repair> createSpecification(RepairCriteria criteria) {
        Specifications<Repair> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Repair_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Repair_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Repair_.enabled));
            }
        }
        return specification;
    }

}
