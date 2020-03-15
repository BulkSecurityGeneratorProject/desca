package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.RepairTypeService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.RepairTypeDTO;
import mx.gob.scjn.desca.service.dto.RepairTypeCriteria;
import mx.gob.scjn.desca.service.RepairTypeQueryService;
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
 * REST controller for managing RepairType.
 */
@RestController
@RequestMapping("/api")
public class RepairTypeResource {

    private final Logger log = LoggerFactory.getLogger(RepairTypeResource.class);

    private static final String ENTITY_NAME = "repairType";

    private final RepairTypeService repairTypeService;

    private final RepairTypeQueryService repairTypeQueryService;

    public RepairTypeResource(RepairTypeService repairTypeService, RepairTypeQueryService repairTypeQueryService) {
        this.repairTypeService = repairTypeService;
        this.repairTypeQueryService = repairTypeQueryService;
    }

    /**
     * POST  /repair-types : Create a new repairType.
     *
     * @param repairTypeDTO the repairTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repairTypeDTO, or with status 400 (Bad Request) if the repairType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repair-types")
    @Timed
    public ResponseEntity<RepairTypeDTO> createRepairType(@Valid @RequestBody RepairTypeDTO repairTypeDTO) throws URISyntaxException {
        log.debug("REST request to save RepairType : {}", repairTypeDTO);
        if (repairTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new repairType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepairTypeDTO result = repairTypeService.save(repairTypeDTO);
        return ResponseEntity.created(new URI("/api/repair-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repair-types : Updates an existing repairType.
     *
     * @param repairTypeDTO the repairTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repairTypeDTO,
     * or with status 400 (Bad Request) if the repairTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the repairTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repair-types")
    @Timed
    public ResponseEntity<RepairTypeDTO> updateRepairType(@Valid @RequestBody RepairTypeDTO repairTypeDTO) throws URISyntaxException {
        log.debug("REST request to update RepairType : {}", repairTypeDTO);
        if (repairTypeDTO.getId() == null) {
            return createRepairType(repairTypeDTO);
        }
        RepairTypeDTO result = repairTypeService.save(repairTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, repairTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repair-types : get all the repairTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of repairTypes in body
     */
    @GetMapping("/repair-types")
    @Timed
    public ResponseEntity<List<RepairTypeDTO>> getAllRepairTypes(RepairTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RepairTypes by criteria: {}", criteria);
        Page<RepairTypeDTO> page = repairTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repair-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /repair-types/:id : get the "id" repairType.
     *
     * @param id the id of the repairTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repairTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/repair-types/{id}")
    @Timed
    public ResponseEntity<RepairTypeDTO> getRepairType(@PathVariable Long id) {
        log.debug("REST request to get RepairType : {}", id);
        RepairTypeDTO repairTypeDTO = repairTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(repairTypeDTO));
    }

    /**
     * DELETE  /repair-types/:id : delete the "id" repairType.
     *
     * @param id the id of the repairTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repair-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRepairType(@PathVariable Long id) {
        log.debug("REST request to delete RepairType : {}", id);
        repairTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/repair-types?query=:query : search for the repairType corresponding
     * to the query.
     *
     * @param query the query of the repairType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/repair-types")
    @Timed
    public ResponseEntity<List<RepairTypeDTO>> searchRepairTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RepairTypes for query {}", query);
        Page<RepairTypeDTO> page = repairTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/repair-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
