package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.VulnerableGroupService;
import mx.gob.scjn.desca.domain.VulnerableGroup;
import mx.gob.scjn.desca.repository.VulnerableGroupRepository;
import mx.gob.scjn.desca.repository.search.VulnerableGroupSearchRepository;
import mx.gob.scjn.desca.service.dto.VulnerableGroupDTO;
import mx.gob.scjn.desca.service.mapper.VulnerableGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing VulnerableGroup.
 */
@Service
@Transactional
public class VulnerableGroupServiceImpl implements VulnerableGroupService {

    private final Logger log = LoggerFactory.getLogger(VulnerableGroupServiceImpl.class);

    private final VulnerableGroupRepository vulnerableGroupRepository;

    private final VulnerableGroupMapper vulnerableGroupMapper;

    private final VulnerableGroupSearchRepository vulnerableGroupSearchRepository;

    public VulnerableGroupServiceImpl(VulnerableGroupRepository vulnerableGroupRepository, VulnerableGroupMapper vulnerableGroupMapper, VulnerableGroupSearchRepository vulnerableGroupSearchRepository) {
        this.vulnerableGroupRepository = vulnerableGroupRepository;
        this.vulnerableGroupMapper = vulnerableGroupMapper;
        this.vulnerableGroupSearchRepository = vulnerableGroupSearchRepository;
    }

    /**
     * Save a vulnerableGroup.
     *
     * @param vulnerableGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VulnerableGroupDTO save(VulnerableGroupDTO vulnerableGroupDTO) {
        log.debug("Request to save VulnerableGroup : {}", vulnerableGroupDTO);
        VulnerableGroup vulnerableGroup = vulnerableGroupMapper.toEntity(vulnerableGroupDTO);
        vulnerableGroup = vulnerableGroupRepository.save(vulnerableGroup);
        VulnerableGroupDTO result = vulnerableGroupMapper.toDto(vulnerableGroup);
        vulnerableGroupSearchRepository.save(vulnerableGroup);
        return result;
    }

    /**
     * Get all the vulnerableGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VulnerableGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VulnerableGroups");
        return vulnerableGroupRepository.findAll(pageable)
            .map(vulnerableGroupMapper::toDto);
    }

    /**
     * Get one vulnerableGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VulnerableGroupDTO findOne(Long id) {
        log.debug("Request to get VulnerableGroup : {}", id);
        VulnerableGroup vulnerableGroup = vulnerableGroupRepository.findOne(id);
        return vulnerableGroupMapper.toDto(vulnerableGroup);
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
        vulnerableGroupSearchRepository.delete(id);
    }

    /**
     * Search for the vulnerableGroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VulnerableGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of VulnerableGroups for query {}", query);
        Page<VulnerableGroup> result = vulnerableGroupSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(vulnerableGroupMapper::toDto);
    }
}
