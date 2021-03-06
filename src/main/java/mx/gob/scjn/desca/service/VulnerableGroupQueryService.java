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

import mx.gob.scjn.desca.domain.VulnerableGroup;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.VulnerableGroupRepository;
import mx.gob.scjn.desca.service.dto.VulnerableGroupCriteria;


/**
 * Service for executing complex queries for VulnerableGroup entities in the database.
 * The main input is a {@link VulnerableGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VulnerableGroup} or a {@link Page} of {@link VulnerableGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VulnerableGroupQueryService extends QueryService<VulnerableGroup> {

    private final Logger log = LoggerFactory.getLogger(VulnerableGroupQueryService.class);


    private final VulnerableGroupRepository vulnerableGroupRepository;

    public VulnerableGroupQueryService(VulnerableGroupRepository vulnerableGroupRepository) {
        this.vulnerableGroupRepository = vulnerableGroupRepository;
    }

    /**
     * Return a {@link List} of {@link VulnerableGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VulnerableGroup> findByCriteria(VulnerableGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<VulnerableGroup> specification = createSpecification(criteria);
        return vulnerableGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link VulnerableGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VulnerableGroup> findByCriteria(VulnerableGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<VulnerableGroup> specification = createSpecification(criteria);
        return vulnerableGroupRepository.findAll(specification, page);
    }

    /**
     * Function to convert VulnerableGroupCriteria to a {@link Specifications}
     */
    private Specifications<VulnerableGroup> createSpecification(VulnerableGroupCriteria criteria) {
        Specifications<VulnerableGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), VulnerableGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), VulnerableGroup_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), VulnerableGroup_.status));
            }
        }
        return specification;
    }

}
