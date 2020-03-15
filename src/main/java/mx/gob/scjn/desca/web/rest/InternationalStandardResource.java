package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.InternationalStandardService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;
import mx.gob.scjn.desca.service.dto.InternationalStandardCriteria;
import mx.gob.scjn.desca.service.InternationalStandardQueryService;
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
 * REST controller for managing InternationalStandard.
 */
@RestController
@RequestMapping("/api")
public class InternationalStandardResource {

    private final Logger log = LoggerFactory.getLogger(InternationalStandardResource.class);

    private static final String ENTITY_NAME = "internationalStandard";

    private final InternationalStandardService internationalStandardService;

    private final InternationalStandardQueryService internationalStandardQueryService;

    public InternationalStandardResource(InternationalStandardService internationalStandardService, InternationalStandardQueryService internationalStandardQueryService) {
        this.internationalStandardService = internationalStandardService;
        this.internationalStandardQueryService = internationalStandardQueryService;
    }

    /**
     * POST  /international-standards : Create a new internationalStandard.
     *
     * @param internationalStandardDTO the internationalStandardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internationalStandardDTO, or with status 400 (Bad Request) if the internationalStandard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/international-standards")
    @Timed
    public ResponseEntity<InternationalStandardDTO> createInternationalStandard(@Valid @RequestBody InternationalStandardDTO internationalStandardDTO) throws URISyntaxException {
        log.debug("REST request to save InternationalStandard : {}", internationalStandardDTO);
        if (internationalStandardDTO.getId() != null) {
            throw new BadRequestAlertException("A new internationalStandard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternationalStandardDTO result = internationalStandardService.save(internationalStandardDTO);
        return ResponseEntity.created(new URI("/api/international-standards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /international-standards : Updates an existing internationalStandard.
     *
     * @param internationalStandardDTO the internationalStandardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internationalStandardDTO,
     * or with status 400 (Bad Request) if the internationalStandardDTO is not valid,
     * or with status 500 (Internal Server Error) if the internationalStandardDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/international-standards")
    @Timed
    public ResponseEntity<InternationalStandardDTO> updateInternationalStandard(@Valid @RequestBody InternationalStandardDTO internationalStandardDTO) throws URISyntaxException {
        log.debug("REST request to update InternationalStandard : {}", internationalStandardDTO);
        if (internationalStandardDTO.getId() == null) {
            return createInternationalStandard(internationalStandardDTO);
        }
        InternationalStandardDTO result = internationalStandardService.save(internationalStandardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, internationalStandardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /international-standards : get all the internationalStandards.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of internationalStandards in body
     */
    @GetMapping("/international-standards")
    @Timed
    public ResponseEntity<List<InternationalStandardDTO>> getAllInternationalStandards(InternationalStandardCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InternationalStandards by criteria: {}", criteria);
        Page<InternationalStandardDTO> page = internationalStandardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/international-standards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /international-standards/:id : get the "id" internationalStandard.
     *
     * @param id the id of the internationalStandardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internationalStandardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/international-standards/{id}")
    @Timed
    public ResponseEntity<InternationalStandardDTO> getInternationalStandard(@PathVariable Long id) {
        log.debug("REST request to get InternationalStandard : {}", id);
        InternationalStandardDTO internationalStandardDTO = internationalStandardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(internationalStandardDTO));
    }

    /**
     * DELETE  /international-standards/:id : delete the "id" internationalStandard.
     *
     * @param id the id of the internationalStandardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/international-standards/{id}")
    @Timed
    public ResponseEntity<Void> deleteInternationalStandard(@PathVariable Long id) {
        log.debug("REST request to delete InternationalStandard : {}", id);
        internationalStandardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/international-standards?query=:query : search for the internationalStandard corresponding
     * to the query.
     *
     * @param query the query of the internationalStandard search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/international-standards")
    @Timed
    public ResponseEntity<List<InternationalStandardDTO>> searchInternationalStandards(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InternationalStandards for query {}", query);
        Page<InternationalStandardDTO> page = internationalStandardService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/international-standards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
