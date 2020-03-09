package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.service.MemberStateService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.MemberStateCriteria;
import mx.gob.scjn.desca.service.MemberStateQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MemberState.
 */
@RestController
@RequestMapping("/api")
public class MemberStateResource {

    private final Logger log = LoggerFactory.getLogger(MemberStateResource.class);

    private static final String ENTITY_NAME = "memberState";

    private final MemberStateService memberStateService;

    private final MemberStateQueryService memberStateQueryService;

    public MemberStateResource(MemberStateService memberStateService, MemberStateQueryService memberStateQueryService) {
        this.memberStateService = memberStateService;
        this.memberStateQueryService = memberStateQueryService;
    }

    /**
     * POST  /member-states : Create a new memberState.
     *
     * @param memberState the memberState to create
     * @return the ResponseEntity with status 201 (Created) and with body the new memberState, or with status 400 (Bad Request) if the memberState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/member-states")
    @Timed
    public ResponseEntity<MemberState> createMemberState(@Valid @RequestBody MemberState memberState) throws URISyntaxException {
        log.debug("REST request to save MemberState : {}", memberState);
        if (memberState.getId() != null) {
            throw new BadRequestAlertException("A new memberState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberState result = memberStateService.save(memberState);
        return ResponseEntity.created(new URI("/api/member-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /member-states : Updates an existing memberState.
     *
     * @param memberState the memberState to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated memberState,
     * or with status 400 (Bad Request) if the memberState is not valid,
     * or with status 500 (Internal Server Error) if the memberState couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/member-states")
    @Timed
    public ResponseEntity<MemberState> updateMemberState(@Valid @RequestBody MemberState memberState) throws URISyntaxException {
        log.debug("REST request to update MemberState : {}", memberState);
        if (memberState.getId() == null) {
            return createMemberState(memberState);
        }
        MemberState result = memberStateService.save(memberState);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, memberState.getId().toString()))
            .body(result);
    }

    /**
     * GET  /member-states : get all the memberStates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of memberStates in body
     */
    @GetMapping("/member-states")
    @Timed
    public ResponseEntity<List<MemberState>> getAllMemberStates(MemberStateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MemberStates by criteria: {}", criteria);
        Page<MemberState> page = memberStateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/member-states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /member-states/:id : get the "id" memberState.
     *
     * @param id the id of the memberState to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the memberState, or with status 404 (Not Found)
     */
    @GetMapping("/member-states/{id}")
    @Timed
    public ResponseEntity<MemberState> getMemberState(@PathVariable Long id) {
        log.debug("REST request to get MemberState : {}", id);
        MemberState memberState = memberStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(memberState));
    }

    /**
     * DELETE  /member-states/:id : delete the "id" memberState.
     *
     * @param id the id of the memberState to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/member-states/{id}")
    @Timed
    public ResponseEntity<Void> deleteMemberState(@PathVariable Long id) {
        log.debug("REST request to delete MemberState : {}", id);
        memberStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
