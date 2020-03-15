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

import mx.gob.scjn.desca.domain.RepairType;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.RepairTypeRepository;
import mx.gob.scjn.desca.repository.search.RepairTypeSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairTypeCriteria;

import mx.gob.scjn.desca.service.dto.RepairTypeDTO;
import mx.gob.scjn.desca.service.mapper.RepairTypeMapper;

/**
 * Service for executing complex queries for RepairType entities in the database.
 * The main input is a {@link RepairTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RepairTypeDTO} or a {@link Page} of {@link RepairTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairTypeQueryService extends QueryService<RepairType> {

    private final Logger log = LoggerFactory.getLogger(RepairTypeQueryService.class);


    private final RepairTypeRepository repairTypeRepository;

    private final RepairTypeMapper repairTypeMapper;

    private final RepairTypeSearchRepository repairTypeSearchRepository;

    public RepairTypeQueryService(RepairTypeRepository repairTypeRepository, RepairTypeMapper repairTypeMapper, RepairTypeSearchRepository repairTypeSearchRepository) {
        this.repairTypeRepository = repairTypeRepository;
        this.repairTypeMapper = repairTypeMapper;
        this.repairTypeSearchRepository = repairTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RepairTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RepairTypeDTO> findByCriteria(RepairTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<RepairType> specification = createSpecification(criteria);
        return repairTypeMapper.toDto(repairTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RepairTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairTypeDTO> findByCriteria(RepairTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<RepairType> specification = createSpecification(criteria);
        final Page<RepairType> result = repairTypeRepository.findAll(specification, page);
        return result.map(repairTypeMapper::toDto);
    }

    /**
     * Function to convert RepairTypeCriteria to a {@link Specifications}
     */
    private Specifications<RepairType> createSpecification(RepairTypeCriteria criteria) {
        Specifications<RepairType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RepairType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RepairType_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), RepairType_.enabled));
            }
        }
        return specification;
    }

}
