package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.domain.Desca;
import mx.gob.scjn.desca.service.DescaService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
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
     * @param desca the desca to create
     * @return the ResponseEntity with status 201 (Created) and with body the new desca, or with status 400 (Bad Request) if the desca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/descas")
    @Timed
    public ResponseEntity<Desca> createDesca(@Valid @RequestBody Desca desca) throws URISyntaxException {
        log.debug("REST request to save Desca : {}", desca);
        if (desca.getId() != null) {
            throw new BadRequestAlertException("A new desca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Desca result = descaService.save(desca);
        return ResponseEntity.created(new URI("/api/descas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descas : Updates an existing desca.
     *
     * @param desca the desca to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated desca,
     * or with status 400 (Bad Request) if the desca is not valid,
     * or with status 500 (Internal Server Error) if the desca couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/descas")
    @Timed
    public ResponseEntity<Desca> updateDesca(@Valid @RequestBody Desca desca) throws URISyntaxException {
        log.debug("REST request to update Desca : {}", desca);
        if (desca.getId() == null) {
            return createDesca(desca);
        }
        Desca result = descaService.save(desca);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, desca.getId().toString()))
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
    public ResponseEntity<List<Desca>> getAllDescas(DescaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Descas by criteria: {}", criteria);
        Page<Desca> page = descaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/descas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /descas/:id : get the "id" desca.
     *
     * @param id the id of the desca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the desca, or with status 404 (Not Found)
     */
    @GetMapping("/descas/{id}")
    @Timed
    public ResponseEntity<Desca> getDesca(@PathVariable Long id) {
        log.debug("REST request to get Desca : {}", id);
        Desca desca = descaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(desca));
    }

    /**
     * DELETE  /descas/:id : delete the "id" desca.
     *
     * @param id the id of the desca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descas/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesca(@PathVariable Long id) {
        log.debug("REST request to delete Desca : {}", id);
        descaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
