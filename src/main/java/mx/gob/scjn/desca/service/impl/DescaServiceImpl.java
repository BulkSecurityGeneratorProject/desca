package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.DescaService;
import mx.gob.scjn.desca.domain.Desca;
import mx.gob.scjn.desca.repository.DescaRepository;
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

    public DescaServiceImpl(DescaRepository descaRepository) {
        this.descaRepository = descaRepository;
    }

    /**
     * Save a desca.
     *
     * @param desca the entity to save
     * @return the persisted entity
     */
    @Override
    public Desca save(Desca desca) {
        log.debug("Request to save Desca : {}", desca);
        return descaRepository.save(desca);
    }

    /**
     * Get all the descas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Desca> findAll(Pageable pageable) {
        log.debug("Request to get all Descas");
        return descaRepository.findAll(pageable);
    }

    /**
     * Get one desca by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Desca findOne(Long id) {
        log.debug("Request to get Desca : {}", id);
        return descaRepository.findOne(id);
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
