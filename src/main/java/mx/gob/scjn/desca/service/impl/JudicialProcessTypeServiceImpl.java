package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.JudicialProcessTypeService;
import mx.gob.scjn.desca.domain.JudicialProcessType;
import mx.gob.scjn.desca.repository.JudicialProcessTypeRepository;
import mx.gob.scjn.desca.repository.search.JudicialProcessTypeSearchRepository;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import mx.gob.scjn.desca.service.mapper.JudicialProcessTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing JudicialProcessType.
 */
@Service
@Transactional
public class JudicialProcessTypeServiceImpl implements JudicialProcessTypeService {

    private final Logger log = LoggerFactory.getLogger(JudicialProcessTypeServiceImpl.class);

    private final JudicialProcessTypeRepository judicialProcessTypeRepository;

    private final JudicialProcessTypeMapper judicialProcessTypeMapper;

    private final JudicialProcessTypeSearchRepository judicialProcessTypeSearchRepository;

    public JudicialProcessTypeServiceImpl(JudicialProcessTypeRepository judicialProcessTypeRepository, JudicialProcessTypeMapper judicialProcessTypeMapper, JudicialProcessTypeSearchRepository judicialProcessTypeSearchRepository) {
        this.judicialProcessTypeRepository = judicialProcessTypeRepository;
        this.judicialProcessTypeMapper = judicialProcessTypeMapper;
        this.judicialProcessTypeSearchRepository = judicialProcessTypeSearchRepository;
    }

    /**
     * Save a judicialProcessType.
     *
     * @param judicialProcessTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JudicialProcessTypeDTO save(JudicialProcessTypeDTO judicialProcessTypeDTO) {
        log.debug("Request to save JudicialProcessType : {}", judicialProcessTypeDTO);
        JudicialProcessType judicialProcessType = judicialProcessTypeMapper.toEntity(judicialProcessTypeDTO);
        judicialProcessType = judicialProcessTypeRepository.save(judicialProcessType);
        JudicialProcessTypeDTO result = judicialProcessTypeMapper.toDto(judicialProcessType);
        judicialProcessTypeSearchRepository.save(judicialProcessType);
        return result;
    }

    /**
     * Get all the judicialProcessTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JudicialProcessTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JudicialProcessTypes");
        return judicialProcessTypeRepository.findAll(pageable)
            .map(judicialProcessTypeMapper::toDto);
    }

    /**
     * Get one judicialProcessType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JudicialProcessTypeDTO findOne(Long id) {
        log.debug("Request to get JudicialProcessType : {}", id);
        JudicialProcessType judicialProcessType = judicialProcessTypeRepository.findOne(id);
        return judicialProcessTypeMapper.toDto(judicialProcessType);
    }

    /**
     * Delete the judicialProcessType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JudicialProcessType : {}", id);
        judicialProcessTypeRepository.delete(id);
        judicialProcessTypeSearchRepository.delete(id);
    }

    /**
     * Search for the judicialProcessType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JudicialProcessTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JudicialProcessTypes for query {}", query);
        Page<JudicialProcessType> result = judicialProcessTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(judicialProcessTypeMapper::toDto);
    }
}
