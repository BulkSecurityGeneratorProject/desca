package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MethodologyService;
import mx.gob.scjn.desca.domain.Methodology;
import mx.gob.scjn.desca.repository.MethodologyRepository;
import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import mx.gob.scjn.desca.service.mapper.MethodologyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Methodology.
 */
@Service
@Transactional
public class MethodologyServiceImpl implements MethodologyService {

    private final Logger log = LoggerFactory.getLogger(MethodologyServiceImpl.class);

    private final MethodologyRepository methodologyRepository;

    private final MethodologyMapper methodologyMapper;

    public MethodologyServiceImpl(MethodologyRepository methodologyRepository, MethodologyMapper methodologyMapper) {
        this.methodologyRepository = methodologyRepository;
        this.methodologyMapper = methodologyMapper;
    }

    /**
     * Save a methodology.
     *
     * @param methodologyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MethodologyDTO save(MethodologyDTO methodologyDTO) {
        log.debug("Request to save Methodology : {}", methodologyDTO);
        Methodology methodology = methodologyMapper.toEntity(methodologyDTO);
        methodology = methodologyRepository.save(methodology);
        return methodologyMapper.toDto(methodology);
    }

    /**
     * Get all the methodologies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MethodologyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Methodologies");
        return methodologyRepository.findAll(pageable)
            .map(methodologyMapper::toDto);
    }

    /**
     * Get one methodology by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MethodologyDTO findOne(Long id) {
        log.debug("Request to get Methodology : {}", id);
        Methodology methodology = methodologyRepository.findOne(id);
        return methodologyMapper.toDto(methodology);
    }

    /**
     * Delete the methodology by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Methodology : {}", id);
        methodologyRepository.delete(id);
    }
}
