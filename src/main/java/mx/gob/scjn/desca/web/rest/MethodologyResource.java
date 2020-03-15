package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.MethodologyService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import mx.gob.scjn.desca.service.dto.MethodologyCriteria;
import mx.gob.scjn.desca.service.MethodologyQueryService;
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
 * REST controller for managing Methodology.
 */
@RestController
@RequestMapping("/api")
public class MethodologyResource {

    private final Logger log = LoggerFactory.getLogger(MethodologyResource.class);

    private static final String ENTITY_NAME = "methodology";

    private final MethodologyService methodologyService;

    private final MethodologyQueryService methodologyQueryService;

    public MethodologyResource(MethodologyService methodologyService, MethodologyQueryService methodologyQueryService) {
        this.methodologyService = methodologyService;
        this.methodologyQueryService = methodologyQueryService;
    }

    /**
     * POST  /methodologies : Create a new methodology.
     *
     * @param methodologyDTO the methodologyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new methodologyDTO, or with status 400 (Bad Request) if the methodology has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/methodologies")
    @Timed
    public ResponseEntity<MethodologyDTO> createMethodology(@Valid @RequestBody MethodologyDTO methodologyDTO) throws URISyntaxException {
        log.debug("REST request to save Methodology : {}", methodologyDTO);
        if (methodologyDTO.getId() != null) {
            throw new BadRequestAlertException("A new methodology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MethodologyDTO result = methodologyService.save(methodologyDTO);
        return ResponseEntity.created(new URI("/api/methodologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /methodologies : Updates an existing methodology.
     *
     * @param methodologyDTO the methodologyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated methodologyDTO,
     * or with status 400 (Bad Request) if the methodologyDTO is not valid,
     * or with status 500 (Internal Server Error) if the methodologyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/methodologies")
    @Timed
    public ResponseEntity<MethodologyDTO> updateMethodology(@Valid @RequestBody MethodologyDTO methodologyDTO) throws URISyntaxException {
        log.debug("REST request to update Methodology : {}", methodologyDTO);
        if (methodologyDTO.getId() == null) {
            return createMethodology(methodologyDTO);
        }
        MethodologyDTO result = methodologyService.save(methodologyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, methodologyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /methodologies : get all the methodologies.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of methodologies in body
     */
    @GetMapping("/methodologies")
    @Timed
    public ResponseEntity<List<MethodologyDTO>> getAllMethodologies(MethodologyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Methodologies by criteria: {}", criteria);
        Page<MethodologyDTO> page = methodologyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/methodologies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /methodologies/:id : get the "id" methodology.
     *
     * @param id the id of the methodologyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the methodologyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/methodologies/{id}")
    @Timed
    public ResponseEntity<MethodologyDTO> getMethodology(@PathVariable Long id) {
        log.debug("REST request to get Methodology : {}", id);
        MethodologyDTO methodologyDTO = methodologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(methodologyDTO));
    }

    /**
     * DELETE  /methodologies/:id : delete the "id" methodology.
     *
     * @param id the id of the methodologyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/methodologies/{id}")
    @Timed
    public ResponseEntity<Void> deleteMethodology(@PathVariable Long id) {
        log.debug("REST request to delete Methodology : {}", id);
        methodologyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/methodologies?query=:query : search for the methodology corresponding
     * to the query.
     *
     * @param query the query of the methodology search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/methodologies")
    @Timed
    public ResponseEntity<List<MethodologyDTO>> searchMethodologies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Methodologies for query {}", query);
        Page<MethodologyDTO> page = methodologyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/methodologies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
