package mx.gob.scjn.desca.service.impl;

import mx.gob.scjn.desca.service.MemberStateService;
import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.repository.MemberStateRepository;
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

    public MemberStateServiceImpl(MemberStateRepository memberStateRepository) {
        this.memberStateRepository = memberStateRepository;
    }

    /**
     * Save a memberState.
     *
     * @param memberState the entity to save
     * @return the persisted entity
     */
    @Override
    public MemberState save(MemberState memberState) {
        log.debug("Request to save MemberState : {}", memberState);
        return memberStateRepository.save(memberState);
    }

    /**
     * Get all the memberStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberState> findAll(Pageable pageable) {
        log.debug("Request to get all MemberStates");
        return memberStateRepository.findAll(pageable);
    }

    /**
     * Get one memberState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MemberState findOne(Long id) {
        log.debug("Request to get MemberState : {}", id);
        return memberStateRepository.findOne(id);
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
