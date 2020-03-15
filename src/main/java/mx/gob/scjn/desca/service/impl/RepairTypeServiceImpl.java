package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.RepairTypeService;
import mx.gob.scjn.desca.domain.RepairType;
import mx.gob.scjn.desca.repository.RepairTypeRepository;
import mx.gob.scjn.desca.repository.search.RepairTypeSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairTypeDTO;
import mx.gob.scjn.desca.service.mapper.RepairTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RepairType.
 */
@Service
@Transactional
public class RepairTypeServiceImpl implements RepairTypeService {

    private final Logger log = LoggerFactory.getLogger(RepairTypeServiceImpl.class);

    private final RepairTypeRepository repairTypeRepository;

    private final RepairTypeMapper repairTypeMapper;

    private final RepairTypeSearchRepository repairTypeSearchRepository;

    public RepairTypeServiceImpl(RepairTypeRepository repairTypeRepository, RepairTypeMapper repairTypeMapper, RepairTypeSearchRepository repairTypeSearchRepository) {
        this.repairTypeRepository = repairTypeRepository;
        this.repairTypeMapper = repairTypeMapper;
        this.repairTypeSearchRepository = repairTypeSearchRepository;
    }

    /**
     * Save a repairType.
     *
     * @param repairTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RepairTypeDTO save(RepairTypeDTO repairTypeDTO) {
        log.debug("Request to save RepairType : {}", repairTypeDTO);
        RepairType repairType = repairTypeMapper.toEntity(repairTypeDTO);
        repairType = repairTypeRepository.save(repairType);
        RepairTypeDTO result = repairTypeMapper.toDto(repairType);
        repairTypeSearchRepository.save(repairType);
        return result;
    }

    /**
     * Get all the repairTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepairTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RepairTypes");
        return repairTypeRepository.findAll(pageable)
            .map(repairTypeMapper::toDto);
    }

    /**
     * Get one repairType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RepairTypeDTO findOne(Long id) {
        log.debug("Request to get RepairType : {}", id);
        RepairType repairType = repairTypeRepository.findOne(id);
        return repairTypeMapper.toDto(repairType);
    }

    /**
     * Delete the repairType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RepairType : {}", id);
        repairTypeRepository.delete(id);
        repairTypeSearchRepository.delete(id);
    }

    /**
     * Search for the repairType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepairTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RepairTypes for query {}", query);
        Page<RepairType> result = repairTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(repairTypeMapper::toDto);
    }
}
