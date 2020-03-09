package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MetodologyService;
import mx.gob.scjn.desca.domain.Metodology;
import mx.gob.scjn.desca.repository.MetodologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Metodology.
 */
@Service
@Transactional
public class MetodologyServiceImpl implements MetodologyService {

    private final Logger log = LoggerFactory.getLogger(MetodologyServiceImpl.class);

    private final MetodologyRepository metodologyRepository;

    public MetodologyServiceImpl(MetodologyRepository metodologyRepository) {
        this.metodologyRepository = metodologyRepository;
    }

    /**
     * Save a metodology.
     *
     * @param metodology the entity to save
     * @return the persisted entity
     */
    @Override
    public Metodology save(Metodology metodology) {
        log.debug("Request to save Metodology : {}", metodology);
        return metodologyRepository.save(metodology);
    }

    /**
     * Get all the metodologies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Metodology> findAll(Pageable pageable) {
        log.debug("Request to get all Metodologies");
        return metodologyRepository.findAll(pageable);
    }

    /**
     * Get one metodology by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Metodology findOne(Long id) {
        log.debug("Request to get Metodology : {}", id);
        return metodologyRepository.findOne(id);
    }

    /**
     * Delete the metodology by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Metodology : {}", id);
        metodologyRepository.delete(id);
    }
}
