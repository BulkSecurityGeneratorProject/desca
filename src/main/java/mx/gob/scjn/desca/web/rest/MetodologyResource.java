package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.domain.Metodology;
import mx.gob.scjn.desca.service.MetodologyService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.MetodologyCriteria;
import mx.gob.scjn.desca.service.MetodologyQueryService;
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
 * REST controller for managing Metodology.
 */
@RestController
@RequestMapping("/api")
public class MetodologyResource {

    private final Logger log = LoggerFactory.getLogger(MetodologyResource.class);

    private static final String ENTITY_NAME = "metodology";

    private final MetodologyService metodologyService;

    private final MetodologyQueryService metodologyQueryService;

    public MetodologyResource(MetodologyService metodologyService, MetodologyQueryService metodologyQueryService) {
        this.metodologyService = metodologyService;
        this.metodologyQueryService = metodologyQueryService;
    }

    /**
     * POST  /metodologies : Create a new metodology.
     *
     * @param metodology the metodology to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metodology, or with status 400 (Bad Request) if the metodology has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metodologies")
    @Timed
    public ResponseEntity<Metodology> createMetodology(@Valid @RequestBody Metodology metodology) throws URISyntaxException {
        log.debug("REST request to save Metodology : {}", metodology);
        if (metodology.getId() != null) {
            throw new BadRequestAlertException("A new metodology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Metodology result = metodologyService.save(metodology);
        return ResponseEntity.created(new URI("/api/metodologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metodologies : Updates an existing metodology.
     *
     * @param metodology the metodology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metodology,
     * or with status 400 (Bad Request) if the metodology is not valid,
     * or with status 500 (Internal Server Error) if the metodology couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metodologies")
    @Timed
    public ResponseEntity<Metodology> updateMetodology(@Valid @RequestBody Metodology metodology) throws URISyntaxException {
        log.debug("REST request to update Metodology : {}", metodology);
        if (metodology.getId() == null) {
            return createMetodology(metodology);
        }
        Metodology result = metodologyService.save(metodology);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metodology.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metodologies : get all the metodologies.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of metodologies in body
     */
    @GetMapping("/metodologies")
    @Timed
    public ResponseEntity<List<Metodology>> getAllMetodologies(MetodologyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Metodologies by criteria: {}", criteria);
        Page<Metodology> page = metodologyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metodologies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metodologies/:id : get the "id" metodology.
     *
     * @param id the id of the metodology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metodology, or with status 404 (Not Found)
     */
    @GetMapping("/metodologies/{id}")
    @Timed
    public ResponseEntity<Metodology> getMetodology(@PathVariable Long id) {
        log.debug("REST request to get Metodology : {}", id);
        Metodology metodology = metodologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metodology));
    }

    /**
     * DELETE  /metodologies/:id : delete the "id" metodology.
     *
     * @param id the id of the metodology to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metodologies/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetodology(@PathVariable Long id) {
        log.debug("REST request to delete Metodology : {}", id);
        metodologyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
