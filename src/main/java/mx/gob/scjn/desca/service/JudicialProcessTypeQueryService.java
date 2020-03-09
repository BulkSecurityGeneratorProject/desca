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

import mx.gob.scjn.desca.domain.JudicialProcessType;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.JudicialProcessTypeRepository;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeCriteria;

import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import mx.gob.scjn.desca.service.mapper.JudicialProcessTypeMapper;

/**
 * Service for executing complex queries for JudicialProcessType entities in the database.
 * The main input is a {@link JudicialProcessTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JudicialProcessTypeDTO} or a {@link Page} of {@link JudicialProcessTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JudicialProcessTypeQueryService extends QueryService<JudicialProcessType> {

    private final Logger log = LoggerFactory.getLogger(JudicialProcessTypeQueryService.class);


    private final JudicialProcessTypeRepository judicialProcessTypeRepository;

    private final JudicialProcessTypeMapper judicialProcessTypeMapper;

    public JudicialProcessTypeQueryService(JudicialProcessTypeRepository judicialProcessTypeRepository, JudicialProcessTypeMapper judicialProcessTypeMapper) {
        this.judicialProcessTypeRepository = judicialProcessTypeRepository;
        this.judicialProcessTypeMapper = judicialProcessTypeMapper;
    }

    /**
     * Return a {@link List} of {@link JudicialProcessTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JudicialProcessTypeDTO> findByCriteria(JudicialProcessTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<JudicialProcessType> specification = createSpecification(criteria);
        return judicialProcessTypeMapper.toDto(judicialProcessTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JudicialProcessTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JudicialProcessTypeDTO> findByCriteria(JudicialProcessTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<JudicialProcessType> specification = createSpecification(criteria);
        final Page<JudicialProcessType> result = judicialProcessTypeRepository.findAll(specification, page);
        return result.map(judicialProcessTypeMapper::toDto);
    }

    /**
     * Function to convert JudicialProcessTypeCriteria to a {@link Specifications}
     */
    private Specifications<JudicialProcessType> createSpecification(JudicialProcessTypeCriteria criteria) {
        Specifications<JudicialProcessType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JudicialProcessType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), JudicialProcessType_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), JudicialProcessType_.status));
            }
        }
        return specification;
    }

}
