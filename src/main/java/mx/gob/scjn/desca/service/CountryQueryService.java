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

import mx.gob.scjn.desca.domain.Country;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.CountryRepository;
import mx.gob.scjn.desca.repository.search.CountrySearchRepository;
import mx.gob.scjn.desca.service.dto.CountryCriteria;

import mx.gob.scjn.desca.service.dto.CountryDTO;
import mx.gob.scjn.desca.service.mapper.CountryMapper;

/**
 * Service for executing complex queries for Country entities in the database.
 * The main input is a {@link CountryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountryDTO} or a {@link Page} of {@link CountryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryQueryService extends QueryService<Country> {

    private final Logger log = LoggerFactory.getLogger(CountryQueryService.class);


    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final CountrySearchRepository countrySearchRepository;

    public CountryQueryService(CountryRepository countryRepository, CountryMapper countryMapper, CountrySearchRepository countrySearchRepository) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link CountryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findByCriteria(CountryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Country> specification = createSpecification(criteria);
        return countryMapper.toDto(countryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryDTO> findByCriteria(CountryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Country> specification = createSpecification(criteria);
        final Page<Country> result = countryRepository.findAll(specification, page);
        return result.map(countryMapper::toDto);
    }

    /**
     * Function to convert CountryCriteria to a {@link Specifications}
     */
    private Specifications<Country> createSpecification(CountryCriteria criteria) {
        Specifications<Country> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Country_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Country_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Country_.code));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Country_.enabled));
            }
        }
        return specification;
    }

}
