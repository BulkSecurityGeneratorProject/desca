package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MemberStateService;
import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.repository.MemberStateRepository;
import mx.gob.scjn.desca.service.dto.MemberStateDTO;
import mx.gob.scjn.desca.service.mapper.MemberStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MemberState.
 */
@Service
@Transactional
public class MemberStateServiceImpl implements MemberStateService {

    private final Logger log = LoggerFactory.getLogger(MemberStateServiceImpl.class);

    private final MemberStateRepository memberStateRepository;

    private final MemberStateMapper memberStateMapper;

    public MemberStateServiceImpl(MemberStateRepository memberStateRepository, MemberStateMapper memberStateMapper) {
        this.memberStateRepository = memberStateRepository;
        this.memberStateMapper = memberStateMapper;
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
        return memberStateMapper.toDto(memberState);
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
    }
}
