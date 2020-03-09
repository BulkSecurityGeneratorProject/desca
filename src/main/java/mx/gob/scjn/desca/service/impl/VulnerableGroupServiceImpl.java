package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.VulnerableGroupService;
import mx.gob.scjn.desca.domain.VulnerableGroup;
import mx.gob.scjn.desca.repository.VulnerableGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing VulnerableGroup.
 */
@Service
@Transactional
public class VulnerableGroupServiceImpl implements VulnerableGroupService {

    private final Logger log = LoggerFactory.getLogger(VulnerableGroupServiceImpl.class);

    private final VulnerableGroupRepository vulnerableGroupRepository;

    public VulnerableGroupServiceImpl(VulnerableGroupRepository vulnerableGroupRepository) {
        this.vulnerableGroupRepository = vulnerableGroupRepository;
    }

    /**
     * Save a vulnerableGroup.
     *
     * @param vulnerableGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public VulnerableGroup save(VulnerableGroup vulnerableGroup) {
        log.debug("Request to save VulnerableGroup : {}", vulnerableGroup);
        return vulnerableGroupRepository.save(vulnerableGroup);
    }

    /**
     * Get all the vulnerableGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VulnerableGroup> findAll(Pageable pageable) {
        log.debug("Request to get all VulnerableGroups");
        return vulnerableGroupRepository.findAll(pageable);
    }

    /**
     * Get one vulnerableGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VulnerableGroup findOne(Long id) {
        log.debug("Request to get VulnerableGroup : {}", id);
        return vulnerableGroupRepository.findOne(id);
    }

    /**
     * Delete the vulnerableGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VulnerableGroup : {}", id);
        vulnerableGroupRepository.delete(id);
    }
}
