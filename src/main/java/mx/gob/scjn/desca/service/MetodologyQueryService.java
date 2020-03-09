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

import mx.gob.scjn.desca.domain.Metodology;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.MetodologyRepository;
import mx.gob.scjn.desca.service.dto.MetodologyCriteria;


/**
 * Service for executing complex queries for Metodology entities in the database.
 * The main input is a {@link MetodologyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Metodology} or a {@link Page} of {@link Metodology} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetodologyQueryService extends QueryService<Metodology> {

    private final Logger log = LoggerFactory.getLogger(MetodologyQueryService.class);


    private final MetodologyRepository metodologyRepository;

    public MetodologyQueryService(MetodologyRepository metodologyRepository) {
        this.metodologyRepository = metodologyRepository;
    }

    /**
     * Return a {@link List} of {@link Metodology} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Metodology> findByCriteria(MetodologyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Metodology> specification = createSpecification(criteria);
        return metodologyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Metodology} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Metodology> findByCriteria(MetodologyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Metodology> specification = createSpecification(criteria);
        return metodologyRepository.findAll(specification, page);
    }

    /**
     * Function to convert MetodologyCriteria to a {@link Specifications}
     */
    private Specifications<Metodology> createSpecification(MetodologyCriteria criteria) {
        Specifications<Metodology> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Metodology_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Metodology_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Metodology_.status));
            }
        }
        return specification;
    }

}
