package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.DescaService;
import mx.gob.scjn.desca.domain.Desca;
import mx.gob.scjn.desca.repository.DescaRepository;
import mx.gob.scjn.desca.service.dto.DescaDTO;
import mx.gob.scjn.desca.service.mapper.DescaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Desca.
 */
@Service
@Transactional
public class DescaServiceImpl implements DescaService {

    private final Logger log = LoggerFactory.getLogger(DescaServiceImpl.class);

    private final DescaRepository descaRepository;

    private final DescaMapper descaMapper;

    public DescaServiceImpl(DescaRepository descaRepository, DescaMapper descaMapper) {
        this.descaRepository = descaRepository;
        this.descaMapper = descaMapper;
    }

    /**
     * Save a desca.
     *
     * @param descaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DescaDTO save(DescaDTO descaDTO) {
        log.debug("Request to save Desca : {}", descaDTO);
        Desca desca = descaMapper.toEntity(descaDTO);
        desca = descaRepository.save(desca);
        return descaMapper.toDto(desca);
    }

    /**
     * Get all the descas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DescaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Descas");
        return descaRepository.findAll(pageable)
            .map(descaMapper::toDto);
    }

    /**
     * Get one desca by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DescaDTO findOne(Long id) {
        log.debug("Request to get Desca : {}", id);
        Desca desca = descaRepository.findOne(id);
        return descaMapper.toDto(desca);
    }

    /**
     * Delete the desca by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Desca : {}", id);
        descaRepository.delete(id);
    }
}
