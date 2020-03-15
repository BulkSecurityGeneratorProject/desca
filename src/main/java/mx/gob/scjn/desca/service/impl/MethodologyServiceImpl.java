package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MethodologyService;
import mx.gob.scjn.desca.domain.Methodology;
import mx.gob.scjn.desca.repository.MethodologyRepository;
import mx.gob.scjn.desca.repository.search.MethodologySearchRepository;
import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import mx.gob.scjn.desca.service.mapper.MethodologyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Methodology.
 */
@Service
@Transactional
public class MethodologyServiceImpl implements MethodologyService {

    private final Logger log = LoggerFactory.getLogger(MethodologyServiceImpl.class);

    private final MethodologyRepository methodologyRepository;

    private final MethodologyMapper methodologyMapper;

    private final MethodologySearchRepository methodologySearchRepository;

    public MethodologyServiceImpl(MethodologyRepository methodologyRepository, MethodologyMapper methodologyMapper, MethodologySearchRepository methodologySearchRepository) {
        this.methodologyRepository = methodologyRepository;
        this.methodologyMapper = methodologyMapper;
        this.methodologySearchRepository = methodologySearchRepository;
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
        MethodologyDTO result = methodologyMapper.toDto(methodology);
        methodologySearchRepository.save(methodology);
        return result;
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
        methodologySearchRepository.delete(id);
    }

    /**
     * Search for the methodology corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MethodologyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Methodologies for query {}", query);
        Page<Methodology> result = methodologySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(methodologyMapper::toDto);
    }
}
