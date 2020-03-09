package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.InternationalStandarService;
import mx.gob.scjn.desca.domain.InternationalStandar;
import mx.gob.scjn.desca.repository.InternationalStandarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing InternationalStandar.
 */
@Service
@Transactional
public class InternationalStandarServiceImpl implements InternationalStandarService {

    private final Logger log = LoggerFactory.getLogger(InternationalStandarServiceImpl.class);

    private final InternationalStandarRepository internationalStandarRepository;

    public InternationalStandarServiceImpl(InternationalStandarRepository internationalStandarRepository) {
        this.internationalStandarRepository = internationalStandarRepository;
    }

    /**
     * Save a internationalStandar.
     *
     * @param internationalStandar the entity to save
     * @return the persisted entity
     */
    @Override
    public InternationalStandar save(InternationalStandar internationalStandar) {
        log.debug("Request to save InternationalStandar : {}", internationalStandar);
        return internationalStandarRepository.save(internationalStandar);
    }

    /**
     * Get all the internationalStandars.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InternationalStandar> findAll(Pageable pageable) {
        log.debug("Request to get all InternationalStandars");
        return internationalStandarRepository.findAll(pageable);
    }

    /**
     * Get one internationalStandar by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InternationalStandar findOne(Long id) {
        log.debug("Request to get InternationalStandar : {}", id);
        return internationalStandarRepository.findOne(id);
    }

    /**
     * Delete the internationalStandar by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InternationalStandar : {}", id);
        internationalStandarRepository.delete(id);
    }
}
