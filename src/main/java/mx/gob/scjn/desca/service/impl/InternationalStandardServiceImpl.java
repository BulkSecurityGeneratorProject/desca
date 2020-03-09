package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.InternationalStandardService;
import mx.gob.scjn.desca.domain.InternationalStandard;
import mx.gob.scjn.desca.repository.InternationalStandardRepository;
import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;
import mx.gob.scjn.desca.service.mapper.InternationalStandardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing InternationalStandard.
 */
@Service
@Transactional
public class InternationalStandardServiceImpl implements InternationalStandardService {

    private final Logger log = LoggerFactory.getLogger(InternationalStandardServiceImpl.class);

    private final InternationalStandardRepository internationalStandardRepository;

    private final InternationalStandardMapper internationalStandardMapper;

    public InternationalStandardServiceImpl(InternationalStandardRepository internationalStandardRepository, InternationalStandardMapper internationalStandardMapper) {
        this.internationalStandardRepository = internationalStandardRepository;
        this.internationalStandardMapper = internationalStandardMapper;
    }

    /**
     * Save a internationalStandard.
     *
     * @param internationalStandardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InternationalStandardDTO save(InternationalStandardDTO internationalStandardDTO) {
        log.debug("Request to save InternationalStandard : {}", internationalStandardDTO);
        InternationalStandard internationalStandard = internationalStandardMapper.toEntity(internationalStandardDTO);
        internationalStandard = internationalStandardRepository.save(internationalStandard);
        return internationalStandardMapper.toDto(internationalStandard);
    }

    /**
     * Get all the internationalStandards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InternationalStandardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InternationalStandards");
        return internationalStandardRepository.findAll(pageable)
            .map(internationalStandardMapper::toDto);
    }

    /**
     * Get one internationalStandard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InternationalStandardDTO findOne(Long id) {
        log.debug("Request to get InternationalStandard : {}", id);
        InternationalStandard internationalStandard = internationalStandardRepository.findOne(id);
        return internationalStandardMapper.toDto(internationalStandard);
    }

    /**
     * Delete the internationalStandard by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InternationalStandard : {}", id);
        internationalStandardRepository.delete(id);
    }
}
