package mx.gob.scjn.desca.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.domain.*; // for static metamodels
import mx.gob.scjn.desca.repository.MemberStateRepository;
import mx.gob.scjn.desca.service.dto.MemberStateCriteria;

import mx.gob.scjn.desca.service.dto.MemberStateDTO;
import mx.gob.scjn.desca.service.mapper.MemberStateMapper;

/**
 * Service for executing complex queries for MemberState entities in the database.
 * The main input is a {@link MemberStateCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberStateDTO} or a {@link Page} of {@link MemberStateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberStateQueryService extends QueryService<MemberState> {

    private final Logger log = LoggerFactory.getLogger(MemberStateQueryService.class);


    private final MemberStateRepository memberStateRepository;

    private final MemberStateMapper memberStateMapper;

    public MemberStateQueryService(MemberStateRepository memberStateRepository, MemberStateMapper memberStateMapper) {
        this.memberStateRepository = memberStateRepository;
        this.memberStateMapper = memberStateMapper;
    }

    /**
     * Return a {@link List} of {@link MemberStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberStateDTO> findByCriteria(MemberStateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MemberState> specification = createSpecification(criteria);
        return memberStateMapper.toDto(memberStateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MemberStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberStateDTO> findByCriteria(MemberStateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MemberState> specification = createSpecification(criteria);
        final Page<MemberState> result = memberStateRepository.findAll(specification, page);
        return result.map(memberStateMapper::toDto);
    }

    /**
     * Function to convert MemberStateCriteria to a {@link Specifications}
     */
    private Specifications<MemberState> createSpecification(MemberStateCriteria criteria) {
        Specifications<MemberState> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MemberState_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MemberState_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), MemberState_.status));
            }
        }
        return specification;
    }

}
