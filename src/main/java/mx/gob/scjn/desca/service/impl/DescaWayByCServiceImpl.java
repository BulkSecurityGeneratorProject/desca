package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.DescaWayByCService;
import mx.gob.scjn.desca.domain.DescaWayByC;
import mx.gob.scjn.desca.repository.DescaWayByCRepository;
import mx.gob.scjn.desca.repository.search.DescaWayByCSearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayByCMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DescaWayByC.
 */
@Service
@Transactional
public class DescaWayByCServiceImpl implements DescaWayByCService {

    private final Logger log = LoggerFactory.getLogger(DescaWayByCServiceImpl.class);

    private final DescaWayByCRepository descaWayByCRepository;

    private final DescaWayByCMapper descaWayByCMapper;

    private final DescaWayByCSearchRepository descaWayByCSearchRepository;

    public DescaWayByCServiceImpl(DescaWayByCRepository descaWayByCRepository, DescaWayByCMapper descaWayByCMapper, DescaWayByCSearchRepository descaWayByCSearchRepository) {
        this.descaWayByCRepository = descaWayByCRepository;
        this.descaWayByCMapper = descaWayByCMapper;
        this.descaWayByCSearchRepository = descaWayByCSearchRepository;
    }

    /**
     * Save a descaWayByC.
     *
     * @param descaWayByCDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DescaWayByCDTO save(DescaWayByCDTO descaWayByCDTO) {
        log.debug("Request to save DescaWayByC : {}", descaWayByCDTO);
        DescaWayByC descaWayByC = descaWayByCMapper.toEntity(descaWayByCDTO);
        descaWayByC = descaWayByCRepository.save(descaWayByC);
        DescaWayByCDTO result = descaWayByCMapper.toDto(descaWayByC);
        descaWayByCSearchRepository.save(descaWayByC);
        return result;
    }

    /**
     * Get all the descaWayByCS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DescaWayByCDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DescaWayByCS");
        return descaWayByCRepository.findAll(pageable)
            .map(descaWayByCMapper::toDto);
    }

    /**
     * Get one descaWayByC by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DescaWayByCDTO findOne(Long id) {
        log.debug("Request to get DescaWayByC : {}", id);
        DescaWayByC descaWayByC = descaWayByCRepository.findOne(id);
        return descaWayByCMapper.toDto(descaWayByC);
    }

    /**
     * Delete the descaWayByC by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DescaWayByC : {}", id);
        descaWayByCRepository.delete(id);
        descaWayByCSearchRepository.delete(id);
    }

    /**
     * Search for the descaWayByC corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DescaWayByCDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DescaWayByCS for query {}", query);
        Page<DescaWayByC> result = descaWayByCSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(descaWayByCMapper::toDto);
    }
}
