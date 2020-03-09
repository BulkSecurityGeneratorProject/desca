package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.DescaWayService;
import mx.gob.scjn.desca.domain.DescaWay;
import mx.gob.scjn.desca.repository.DescaWayRepository;
import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing DescaWay.
 */
@Service
@Transactional
public class DescaWayServiceImpl implements DescaWayService {

    private final Logger log = LoggerFactory.getLogger(DescaWayServiceImpl.class);

    private final DescaWayRepository descaWayRepository;

    private final DescaWayMapper descaWayMapper;

    public DescaWayServiceImpl(DescaWayRepository descaWayRepository, DescaWayMapper descaWayMapper) {
        this.descaWayRepository = descaWayRepository;
        this.descaWayMapper = descaWayMapper;
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
        return descaWayMapper.toDto(descaWay);
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
    }
}
