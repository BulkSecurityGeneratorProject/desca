package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.RepairService;
import mx.gob.scjn.desca.domain.Repair;
import mx.gob.scjn.desca.repository.RepairRepository;
import mx.gob.scjn.desca.repository.search.RepairSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairDTO;
import mx.gob.scjn.desca.service.mapper.RepairMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Repair.
 */
@Service
@Transactional
public class RepairServiceImpl implements RepairService {

    private final Logger log = LoggerFactory.getLogger(RepairServiceImpl.class);

    private final RepairRepository repairRepository;

    private final RepairMapper repairMapper;

    private final RepairSearchRepository repairSearchRepository;

    public RepairServiceImpl(RepairRepository repairRepository, RepairMapper repairMapper, RepairSearchRepository repairSearchRepository) {
        this.repairRepository = repairRepository;
        this.repairMapper = repairMapper;
        this.repairSearchRepository = repairSearchRepository;
    }

    /**
     * Save a repair.
     *
     * @param repairDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RepairDTO save(RepairDTO repairDTO) {
        log.debug("Request to save Repair : {}", repairDTO);
        Repair repair = repairMapper.toEntity(repairDTO);
        repair = repairRepository.save(repair);
        RepairDTO result = repairMapper.toDto(repair);
        repairSearchRepository.save(repair);
        return result;
    }

    /**
     * Get all the repairs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepairDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repairs");
        return repairRepository.findAll(pageable)
            .map(repairMapper::toDto);
    }

    /**
     * Get one repair by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RepairDTO findOne(Long id) {
        log.debug("Request to get Repair : {}", id);
        Repair repair = repairRepository.findOne(id);
        return repairMapper.toDto(repair);
    }

    /**
     * Delete the repair by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Repair : {}", id);
        repairRepository.delete(id);
        repairSearchRepository.delete(id);
    }

    /**
     * Search for the repair corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepairDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Repairs for query {}", query);
        Page<Repair> result = repairSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(repairMapper::toDto);
    }
}
