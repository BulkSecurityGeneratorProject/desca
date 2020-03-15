package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.InstitutionService;
import mx.gob.scjn.desca.domain.Institution;
import mx.gob.scjn.desca.repository.InstitutionRepository;
import mx.gob.scjn.desca.repository.search.InstitutionSearchRepository;
import mx.gob.scjn.desca.service.dto.InstitutionDTO;
import mx.gob.scjn.desca.service.mapper.InstitutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Institution.
 */
@Service
@Transactional
public class InstitutionServiceImpl implements InstitutionService {

    private final Logger log = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper institutionMapper;

    private final InstitutionSearchRepository institutionSearchRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper, InstitutionSearchRepository institutionSearchRepository) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
        this.institutionSearchRepository = institutionSearchRepository;
    }

    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InstitutionDTO save(InstitutionDTO institutionDTO) {
        log.debug("Request to save Institution : {}", institutionDTO);
        Institution institution = institutionMapper.toEntity(institutionDTO);
        institution = institutionRepository.save(institution);
        InstitutionDTO result = institutionMapper.toDto(institution);
        institutionSearchRepository.save(institution);
        return result;
    }

    /**
     * Get all the institutions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Institutions");
        return institutionRepository.findAll(pageable)
            .map(institutionMapper::toDto);
    }

    /**
     * Get one institution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InstitutionDTO findOne(Long id) {
        log.debug("Request to get Institution : {}", id);
        Institution institution = institutionRepository.findOne(id);
        return institutionMapper.toDto(institution);
    }

    /**
     * Delete the institution by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Institution : {}", id);
        institutionRepository.delete(id);
        institutionSearchRepository.delete(id);
    }

    /**
     * Search for the institution corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Institutions for query {}", query);
        Page<Institution> result = institutionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(institutionMapper::toDto);
    }
}
