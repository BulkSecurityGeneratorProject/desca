package mx.gob.scjn.desca.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mx.gob.scjn.desca.domain.MainDatabase;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.MainDatabaseRepository;
import mx.gob.scjn.desca.repository.search.MainDatabaseSearchRepository;
import mx.gob.scjn.desca.service.dto.MainDatabaseCriteria;

import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;
import mx.gob.scjn.desca.service.mapper.MainDatabaseMapper;

/**
 * Service for executing complex queries for MainDatabase entities in the database.
 * The main input is a {@link MainDatabaseCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MainDatabaseDTO} or a {@link Page} of {@link MainDatabaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MainDatabaseQueryService extends QueryService<MainDatabase> {

    private final Logger log = LoggerFactory.getLogger(MainDatabaseQueryService.class);


    private final MainDatabaseRepository mainDatabaseRepository;

    private final MainDatabaseMapper mainDatabaseMapper;

    private final MainDatabaseSearchRepository mainDatabaseSearchRepository;

    public MainDatabaseQueryService(MainDatabaseRepository mainDatabaseRepository, MainDatabaseMapper mainDatabaseMapper, MainDatabaseSearchRepository mainDatabaseSearchRepository) {
        this.mainDatabaseRepository = mainDatabaseRepository;
        this.mainDatabaseMapper = mainDatabaseMapper;
        this.mainDatabaseSearchRepository = mainDatabaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MainDatabaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MainDatabaseDTO> findByCriteria(MainDatabaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MainDatabase> specification = createSpecification(criteria);
        return mainDatabaseMapper.toDto(mainDatabaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MainDatabaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MainDatabaseDTO> findByCriteria(MainDatabaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MainDatabase> specification = createSpecification(criteria);
        final Page<MainDatabase> result = mainDatabaseRepository.findAll(specification, page);
        return result.map(mainDatabaseMapper::toDto);
    }

    /**
     * Function to convert MainDatabaseCriteria to a {@link Specifications}
     */
    private Specifications<MainDatabase> createSpecification(MainDatabaseCriteria criteria) {
        Specifications<MainDatabase> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MainDatabase_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), MainDatabase_.number));
            }
            if (criteria.getIntitution() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIntitution(), MainDatabase_.intitution));
            }
            if (criteria.getResolutionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResolutionDate(), MainDatabase_.resolutionDate));
            }
            if (criteria.getMemberStateId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMemberStateId(), MainDatabase_.memberState, MemberState_.id));
            }
            if (criteria.getJudicialProcessTypeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getJudicialProcessTypeId(), MainDatabase_.judicialProcessType, JudicialProcessType_.id));
            }
            if (criteria.getDescaWayByCId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDescaWayByCId(), MainDatabase_.descaWayByC, DescaWayByC_.id));
            }
        }
        return specification;
    }

}
