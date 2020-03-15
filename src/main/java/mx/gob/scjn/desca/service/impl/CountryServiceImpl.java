package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.CountryService;
import mx.gob.scjn.desca.domain.Country;
import mx.gob.scjn.desca.repository.CountryRepository;
import mx.gob.scjn.desca.repository.search.CountrySearchRepository;
import mx.gob.scjn.desca.service.dto.CountryDTO;
import mx.gob.scjn.desca.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Country.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final CountrySearchRepository countrySearchRepository;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper, CountrySearchRepository countrySearchRepository) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        Country country = countryMapper.toEntity(countryDTO);
        country = countryRepository.save(country);
        CountryDTO result = countryMapper.toDto(country);
        countrySearchRepository.save(country);
        return result;
    }

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAll(pageable)
            .map(countryMapper::toDto);
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CountryDTO findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        return countryMapper.toDto(country);
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.delete(id);
        countrySearchRepository.delete(id);
    }

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Countries for query {}", query);
        Page<Country> result = countrySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(countryMapper::toDto);
    }
}
