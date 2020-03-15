package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.RepairService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.RepairDTO;
import mx.gob.scjn.desca.service.dto.RepairCriteria;
import mx.gob.scjn.desca.service.RepairQueryService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Repair.
 */
@RestController
@RequestMapping("/api")
public class RepairResource {

    private final Logger log = LoggerFactory.getLogger(RepairResource.class);

    private static final String ENTITY_NAME = "repair";

    private final RepairService repairService;

    private final RepairQueryService repairQueryService;

    public RepairResource(RepairService repairService, RepairQueryService repairQueryService) {
        this.repairService = repairService;
        this.repairQueryService = repairQueryService;
    }

    /**
     * POST  /repairs : Create a new repair.
     *
     * @param repairDTO the repairDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repairDTO, or with status 400 (Bad Request) if the repair has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repairs")
    @Timed
    public ResponseEntity<RepairDTO> createRepair(@Valid @RequestBody RepairDTO repairDTO) throws URISyntaxException {
        log.debug("REST request to save Repair : {}", repairDTO);
        if (repairDTO.getId() != null) {
            throw new BadRequestAlertException("A new repair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepairDTO result = repairService.save(repairDTO);
        return ResponseEntity.created(new URI("/api/repairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repairs : Updates an existing repair.
     *
     * @param repairDTO the repairDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repairDTO,
     * or with status 400 (Bad Request) if the repairDTO is not valid,
     * or with status 500 (Internal Server Error) if the repairDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repairs")
    @Timed
    public ResponseEntity<RepairDTO> updateRepair(@Valid @RequestBody RepairDTO repairDTO) throws URISyntaxException {
        log.debug("REST request to update Repair : {}", repairDTO);
        if (repairDTO.getId() == null) {
            return createRepair(repairDTO);
        }
        RepairDTO result = repairService.save(repairDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, repairDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repairs : get all the repairs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of repairs in body
     */
    @GetMapping("/repairs")
    @Timed
    public ResponseEntity<List<RepairDTO>> getAllRepairs(RepairCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Repairs by criteria: {}", criteria);
        Page<RepairDTO> page = repairQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repairs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /repairs/:id : get the "id" repair.
     *
     * @param id the id of the repairDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repairDTO, or with status 404 (Not Found)
     */
    @GetMapping("/repairs/{id}")
    @Timed
    public ResponseEntity<RepairDTO> getRepair(@PathVariable Long id) {
        log.debug("REST request to get Repair : {}", id);
        RepairDTO repairDTO = repairService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(repairDTO));
    }

    /**
     * DELETE  /repairs/:id : delete the "id" repair.
     *
     * @param id the id of the repairDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repairs/{id}")
    @Timed
    public ResponseEntity<Void> deleteRepair(@PathVariable Long id) {
        log.debug("REST request to delete Repair : {}", id);
        repairService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/repairs?query=:query : search for the repair corresponding
     * to the query.
     *
     * @param query the query of the repair search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/repairs")
    @Timed
    public ResponseEntity<List<RepairDTO>> searchRepairs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Repairs for query {}", query);
        Page<RepairDTO> page = repairService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/repairs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
