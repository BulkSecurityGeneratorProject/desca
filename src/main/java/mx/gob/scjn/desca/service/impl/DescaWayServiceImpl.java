package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.DescaWayService;
import mx.gob.scjn.desca.domain.DescaWay;
import mx.gob.scjn.desca.repository.DescaWayRepository;
import mx.gob.scjn.desca.repository.search.DescaWaySearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DescaWay.
 */
@Service
@Transactional
public class DescaWayServiceImpl implements DescaWayService {

    private final Logger log = LoggerFactory.getLogger(DescaWayServiceImpl.class);

    private final DescaWayRepository descaWayRepository;

    private final DescaWayMapper descaWayMapper;

    private final DescaWaySearchRepository descaWaySearchRepository;

    public DescaWayServiceImpl(DescaWayRepository descaWayRepository, DescaWayMapper descaWayMapper, DescaWaySearchRepository descaWaySearchRepository) {
        this.descaWayRepository = descaWayRepository;
        this.descaWayMapper = descaWayMapper;
        this.descaWaySearchRepository = descaWaySearchRepository;
    }

    /**
     * Save a descaWay.
     *
     * @param descaWayDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DescaWayDTO save(DescaWayDTO descaWayDTO) {
        log.debug("Request to save DescaWay : {}", descaWayDTO);
        DescaWay descaWay = descaWayMapper.toEntity(descaWayDTO);
        descaWay = descaWayRepository.save(descaWay);
        DescaWayDTO result = descaWayMapper.toDto(descaWay);
        descaWaySearchRepository.save(descaWay);
        return result;
    }

    /**
     * Get all the descaWays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DescaWayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DescaWays");
        return descaWayRepository.findAll(pageable)
            .map(descaWayMapper::toDto);
    }

    /**
     * Get one descaWay by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DescaWayDTO findOne(Long id) {
        log.debug("Request to get DescaWay : {}", id);
        DescaWay descaWay = descaWayRepository.findOne(id);
        return descaWayMapper.toDto(descaWay);
    }

    /**
     * Delete the descaWay by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DescaWay : {}", id);
        descaWayRepository.delete(id);
        descaWaySearchRepository.delete(id);
    }

    /**
     * Search for the descaWay corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DescaWayDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DescaWays for query {}", query);
        Page<DescaWay> result = descaWaySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(descaWayMapper::toDto);
    }
}
