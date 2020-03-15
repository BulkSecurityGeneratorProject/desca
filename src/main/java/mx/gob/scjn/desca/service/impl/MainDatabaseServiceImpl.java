package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MainDatabaseService;
import mx.gob.scjn.desca.domain.MainDatabase;
import mx.gob.scjn.desca.repository.MainDatabaseRepository;
import mx.gob.scjn.desca.repository.search.MainDatabaseSearchRepository;
import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;
import mx.gob.scjn.desca.service.mapper.MainDatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MainDatabase.
 */
@Service
@Transactional
public class MainDatabaseServiceImpl implements MainDatabaseService {

    private final Logger log = LoggerFactory.getLogger(MainDatabaseServiceImpl.class);

    private final MainDatabaseRepository mainDatabaseRepository;

    private final MainDatabaseMapper mainDatabaseMapper;

    private final MainDatabaseSearchRepository mainDatabaseSearchRepository;

    public MainDatabaseServiceImpl(MainDatabaseRepository mainDatabaseRepository, MainDatabaseMapper mainDatabaseMapper, MainDatabaseSearchRepository mainDatabaseSearchRepository) {
        this.mainDatabaseRepository = mainDatabaseRepository;
        this.mainDatabaseMapper = mainDatabaseMapper;
        this.mainDatabaseSearchRepository = mainDatabaseSearchRepository;
    }

    /**
     * Save a mainDatabase.
     *
     * @param mainDatabaseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MainDatabaseDTO save(MainDatabaseDTO mainDatabaseDTO) {
        log.debug("Request to save MainDatabase : {}", mainDatabaseDTO);
        MainDatabase mainDatabase = mainDatabaseMapper.toEntity(mainDatabaseDTO);
        mainDatabase = mainDatabaseRepository.save(mainDatabase);
        MainDatabaseDTO result = mainDatabaseMapper.toDto(mainDatabase);
        mainDatabaseSearchRepository.save(mainDatabase);
        return result;
    }

    /**
     * Get all the mainDatabases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MainDatabaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MainDatabases");
        return mainDatabaseRepository.findAll(pageable)
            .map(mainDatabaseMapper::toDto);
    }

    /**
     * Get one mainDatabase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MainDatabaseDTO findOne(Long id) {
        log.debug("Request to get MainDatabase : {}", id);
        MainDatabase mainDatabase = mainDatabaseRepository.findOne(id);
        return mainDatabaseMapper.toDto(mainDatabase);
    }

    /**
     * Delete the mainDatabase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MainDatabase : {}", id);
        mainDatabaseRepository.delete(id);
        mainDatabaseSearchRepository.delete(id);
    }

    /**
     * Search for the mainDatabase corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MainDatabaseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MainDatabases for query {}", query);
        Page<MainDatabase> result = mainDatabaseSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(mainDatabaseMapper::toDto);
    }
}
