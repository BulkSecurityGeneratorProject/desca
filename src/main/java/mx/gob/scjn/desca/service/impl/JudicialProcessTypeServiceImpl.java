package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.JudicialProcessTypeService;
import mx.gob.scjn.desca.domain.JudicialProcessType;
import mx.gob.scjn.desca.repository.JudicialProcessTypeRepository;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import mx.gob.scjn.desca.service.mapper.JudicialProcessTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JudicialProcessType.
 */
@Service
@Transactional
public class JudicialProcessTypeServiceImpl implements JudicialProcessTypeService {

    private final Logger log = LoggerFactory.getLogger(JudicialProcessTypeServiceImpl.class);

    private final JudicialProcessTypeRepository judicialProcessTypeRepository;

    private final JudicialProcessTypeMapper judicialProcessTypeMapper;

    public JudicialProcessTypeServiceImpl(JudicialProcessTypeRepository judicialProcessTypeRepository, JudicialProcessTypeMapper judicialProcessTypeMapper) {
        this.judicialProcessTypeRepository = judicialProcessTypeRepository;
        this.judicialProcessTypeMapper = judicialProcessTypeMapper;
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
        return judicialProcessTypeMapper.toDto(judicialProcessType);
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
    }
}
