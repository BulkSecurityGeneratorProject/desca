package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.DescaService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.DescaDTO;
import mx.gob.scjn.desca.service.dto.DescaCriteria;
import mx.gob.scjn.desca.service.DescaQueryService;
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
 * REST controller for managing Desca.
 */
@RestController
@RequestMapping("/api")
public class DescaResource {

    private final Logger log = LoggerFactory.getLogger(DescaResource.class);

    private static final String ENTITY_NAME = "desca";

    private final DescaService descaService;

    private final DescaQueryService descaQueryService;

    public DescaResource(DescaService descaService, DescaQueryService descaQueryService) {
        this.descaService = descaService;
        this.descaQueryService = descaQueryService;
    }

    /**
     * POST  /descas : Create a new desca.
     *
     * @param descaDTO the descaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descaDTO, or with status 400 (Bad Request) if the desca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/descas")
    @Timed
    public ResponseEntity<DescaDTO> createDesca(@Valid @RequestBody DescaDTO descaDTO) throws URISyntaxException {
        log.debug("REST request to save Desca : {}", descaDTO);
        if (descaDTO.getId() != null) {
            throw new BadRequestAlertException("A new desca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescaDTO result = descaService.save(descaDTO);
        return ResponseEntity.created(new URI("/api/descas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descas : Updates an existing desca.
     *
     * @param descaDTO the descaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descaDTO,
     * or with status 400 (Bad Request) if the descaDTO is not valid,
     * or with status 500 (Internal Server Error) if the descaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/descas")
    @Timed
    public ResponseEntity<DescaDTO> updateDesca(@Valid @RequestBody DescaDTO descaDTO) throws URISyntaxException {
        log.debug("REST request to update Desca : {}", descaDTO);
        if (descaDTO.getId() == null) {
            return createDesca(descaDTO);
        }
        DescaDTO result = descaService.save(descaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /descas : get all the descas.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of descas in body
     */
    @GetMapping("/descas")
    @Timed
    public ResponseEntity<List<DescaDTO>> getAllDescas(DescaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Descas by criteria: {}", criteria);
        Page<DescaDTO> page = descaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/descas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /descas/:id : get the "id" desca.
     *
     * @param id the id of the descaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/descas/{id}")
    @Timed
    public ResponseEntity<DescaDTO> getDesca(@PathVariable Long id) {
        log.debug("REST request to get Desca : {}", id);
        DescaDTO descaDTO = descaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descaDTO));
    }

    /**
     * DELETE  /descas/:id : delete the "id" desca.
     *
     * @param id the id of the descaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descas/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesca(@PathVariable Long id) {
        log.debug("REST request to delete Desca : {}", id);
        descaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/descas?query=:query : search for the desca corresponding
     * to the query.
     *
     * @param query the query of the desca search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/descas")
    @Timed
    public ResponseEntity<List<DescaDTO>> searchDescas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Descas for query {}", query);
        Page<DescaDTO> page = descaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/descas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
