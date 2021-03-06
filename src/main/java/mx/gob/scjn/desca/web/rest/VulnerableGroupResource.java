package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.domain.VulnerableGroup;
import mx.gob.scjn.desca.service.VulnerableGroupService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.VulnerableGroupCriteria;
import mx.gob.scjn.desca.service.VulnerableGroupQueryService;
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
 * REST controller for managing VulnerableGroup.
 */
@RestController
@RequestMapping("/api")
public class VulnerableGroupResource {

    private final Logger log = LoggerFactory.getLogger(VulnerableGroupResource.class);

    private static final String ENTITY_NAME = "vulnerableGroup";

    private final VulnerableGroupService vulnerableGroupService;

    private final VulnerableGroupQueryService vulnerableGroupQueryService;

    public VulnerableGroupResource(VulnerableGroupService vulnerableGroupService, VulnerableGroupQueryService vulnerableGroupQueryService) {
        this.vulnerableGroupService = vulnerableGroupService;
        this.vulnerableGroupQueryService = vulnerableGroupQueryService;
    }

    /**
     * POST  /vulnerable-groups : Create a new vulnerableGroup.
     *
     * @param vulnerableGroup the vulnerableGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vulnerableGroup, or with status 400 (Bad Request) if the vulnerableGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vulnerable-groups")
    @Timed
    public ResponseEntity<VulnerableGroup> createVulnerableGroup(@Valid @RequestBody VulnerableGroup vulnerableGroup) throws URISyntaxException {
        log.debug("REST request to save VulnerableGroup : {}", vulnerableGroup);
        if (vulnerableGroup.getId() != null) {
            throw new BadRequestAlertException("A new vulnerableGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VulnerableGroup result = vulnerableGroupService.save(vulnerableGroup);
        return ResponseEntity.created(new URI("/api/vulnerable-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vulnerable-groups : Updates an existing vulnerableGroup.
     *
     * @param vulnerableGroup the vulnerableGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vulnerableGroup,
     * or with status 400 (Bad Request) if the vulnerableGroup is not valid,
     * or with status 500 (Internal Server Error) if the vulnerableGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vulnerable-groups")
    @Timed
    public ResponseEntity<VulnerableGroup> updateVulnerableGroup(@Valid @RequestBody VulnerableGroup vulnerableGroup) throws URISyntaxException {
        log.debug("REST request to update VulnerableGroup : {}", vulnerableGroup);
        if (vulnerableGroup.getId() == null) {
            return createVulnerableGroup(vulnerableGroup);
        }
        VulnerableGroup result = vulnerableGroupService.save(vulnerableGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vulnerableGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vulnerable-groups : get all the vulnerableGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of vulnerableGroups in body
     */
    @GetMapping("/vulnerable-groups")
    @Timed
    public ResponseEntity<List<VulnerableGroup>> getAllVulnerableGroups(VulnerableGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VulnerableGroups by criteria: {}", criteria);
        Page<VulnerableGroup> page = vulnerableGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vulnerable-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vulnerable-groups/:id : get the "id" vulnerableGroup.
     *
     * @param id the id of the vulnerableGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vulnerableGroup, or with status 404 (Not Found)
     */
    @GetMapping("/vulnerable-groups/{id}")
    @Timed
    public ResponseEntity<VulnerableGroup> getVulnerableGroup(@PathVariable Long id) {
        log.debug("REST request to get VulnerableGroup : {}", id);
        VulnerableGroup vulnerableGroup = vulnerableGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vulnerableGroup));
    }

    /**
     * DELETE  /vulnerable-groups/:id : delete the "id" vulnerableGroup.
     *
     * @param id the id of the vulnerableGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vulnerable-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteVulnerableGroup(@PathVariable Long id) {
        log.debug("REST request to delete VulnerableGroup : {}", id);
        vulnerableGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
