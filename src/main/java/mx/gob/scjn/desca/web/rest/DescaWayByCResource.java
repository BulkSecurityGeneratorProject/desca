package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.DescaWayByCService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;
import mx.gob.scjn.desca.service.dto.DescaWayByCCriteria;
import mx.gob.scjn.desca.service.DescaWayByCQueryService;
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
 * REST controller for managing DescaWayByC.
 */
@RestController
@RequestMapping("/api")
public class DescaWayByCResource {

    private final Logger log = LoggerFactory.getLogger(DescaWayByCResource.class);

    private static final String ENTITY_NAME = "descaWayByC";

    private final DescaWayByCService descaWayByCService;

    private final DescaWayByCQueryService descaWayByCQueryService;

    public DescaWayByCResource(DescaWayByCService descaWayByCService, DescaWayByCQueryService descaWayByCQueryService) {
        this.descaWayByCService = descaWayByCService;
        this.descaWayByCQueryService = descaWayByCQueryService;
    }

    /**
     * POST  /desca-way-by-cs : Create a new descaWayByC.
     *
     * @param descaWayByCDTO the descaWayByCDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descaWayByCDTO, or with status 400 (Bad Request) if the descaWayByC has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/desca-way-by-cs")
    @Timed
    public ResponseEntity<DescaWayByCDTO> createDescaWayByC(@Valid @RequestBody DescaWayByCDTO descaWayByCDTO) throws URISyntaxException {
        log.debug("REST request to save DescaWayByC : {}", descaWayByCDTO);
        if (descaWayByCDTO.getId() != null) {
            throw new BadRequestAlertException("A new descaWayByC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescaWayByCDTO result = descaWayByCService.save(descaWayByCDTO);
        return ResponseEntity.created(new URI("/api/desca-way-by-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /desca-way-by-cs : Updates an existing descaWayByC.
     *
     * @param descaWayByCDTO the descaWayByCDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descaWayByCDTO,
     * or with status 400 (Bad Request) if the descaWayByCDTO is not valid,
     * or with status 500 (Internal Server Error) if the descaWayByCDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/desca-way-by-cs")
    @Timed
    public ResponseEntity<DescaWayByCDTO> updateDescaWayByC(@Valid @RequestBody DescaWayByCDTO descaWayByCDTO) throws URISyntaxException {
        log.debug("REST request to update DescaWayByC : {}", descaWayByCDTO);
        if (descaWayByCDTO.getId() == null) {
            return createDescaWayByC(descaWayByCDTO);
        }
        DescaWayByCDTO result = descaWayByCService.save(descaWayByCDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descaWayByCDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /desca-way-by-cs : get all the descaWayByCS.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of descaWayByCS in body
     */
    @GetMapping("/desca-way-by-cs")
    @Timed
    public ResponseEntity<List<DescaWayByCDTO>> getAllDescaWayByCS(DescaWayByCCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DescaWayByCS by criteria: {}", criteria);
        Page<DescaWayByCDTO> page = descaWayByCQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desca-way-by-cs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desca-way-by-cs/:id : get the "id" descaWayByC.
     *
     * @param id the id of the descaWayByCDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descaWayByCDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desca-way-by-cs/{id}")
    @Timed
    public ResponseEntity<DescaWayByCDTO> getDescaWayByC(@PathVariable Long id) {
        log.debug("REST request to get DescaWayByC : {}", id);
        DescaWayByCDTO descaWayByCDTO = descaWayByCService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descaWayByCDTO));
    }

    /**
     * DELETE  /desca-way-by-cs/:id : delete the "id" descaWayByC.
     *
     * @param id the id of the descaWayByCDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desca-way-by-cs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDescaWayByC(@PathVariable Long id) {
        log.debug("REST request to delete DescaWayByC : {}", id);
        descaWayByCService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/desca-way-by-cs?query=:query : search for the descaWayByC corresponding
     * to the query.
     *
     * @param query the query of the descaWayByC search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/desca-way-by-cs")
    @Timed
    public ResponseEntity<List<DescaWayByCDTO>> searchDescaWayByCS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DescaWayByCS for query {}", query);
        Page<DescaWayByCDTO> page = descaWayByCService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/desca-way-by-cs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
