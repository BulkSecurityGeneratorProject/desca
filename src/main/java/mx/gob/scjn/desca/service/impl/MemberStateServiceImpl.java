package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MemberStateService;
import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.repository.MemberStateRepository;
import mx.gob.scjn.desca.repository.search.MemberStateSearchRepository;
import mx.gob.scjn.desca.service.dto.MemberStateDTO;
import mx.gob.scjn.desca.service.mapper.MemberStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MemberState.
 */
@Service
@Transactional
public class MemberStateServiceImpl implements MemberStateService {

    private final Logger log = LoggerFactory.getLogger(MemberStateServiceImpl.class);

    private final MemberStateRepository memberStateRepository;

    private final MemberStateMapper memberStateMapper;

    private final MemberStateSearchRepository memberStateSearchRepository;

    public MemberStateServiceImpl(MemberStateRepository memberStateRepository, MemberStateMapper memberStateMapper, MemberStateSearchRepository memberStateSearchRepository) {
        this.memberStateRepository = memberStateRepository;
        this.memberStateMapper = memberStateMapper;
        this.memberStateSearchRepository = memberStateSearchRepository;
    }

    /**
     * Save a memberState.
     *
     * @param memberStateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MemberStateDTO save(MemberStateDTO memberStateDTO) {
        log.debug("Request to save MemberState : {}", memberStateDTO);
        MemberState memberState = memberStateMapper.toEntity(memberStateDTO);
        memberState = memberStateRepository.save(memberState);
        MemberStateDTO result = memberStateMapper.toDto(memberState);
        memberStateSearchRepository.save(memberState);
        return result;
    }

    /**
     * Get all the memberStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberStateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MemberStates");
        return memberStateRepository.findAll(pageable)
            .map(memberStateMapper::toDto);
    }

    /**
     * Get one memberState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MemberStateDTO findOne(Long id) {
        log.debug("Request to get MemberState : {}", id);
        MemberState memberState = memberStateRepository.findOne(id);
        return memberStateMapper.toDto(memberState);
    }

    /**
     * Delete the memberState by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemberState : {}", id);
        memberStateRepository.delete(id);
        memberStateSearchRepository.delete(id);
    }

    /**
     * Search for the memberState corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberStateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MemberStates for query {}", query);
        Page<MemberState> result = memberStateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(memberStateMapper::toDto);
    }
}
