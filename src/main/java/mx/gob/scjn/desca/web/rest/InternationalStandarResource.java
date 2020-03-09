package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.domain.InternationalStandar;
import mx.gob.scjn.desca.service.InternationalStandarService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.InternationalStandarCriteria;
import mx.gob.scjn.desca.service.InternationalStandarQueryService;
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
 * REST controller for managing InternationalStandar.
 */
@RestController
@RequestMapping("/api")
public class InternationalStandarResource {

    private final Logger log = LoggerFactory.getLogger(InternationalStandarResource.class);

    private static final String ENTITY_NAME = "internationalStandar";

    private final InternationalStandarService internationalStandarService;

    private final InternationalStandarQueryService internationalStandarQueryService;

    public InternationalStandarResource(InternationalStandarService internationalStandarService, InternationalStandarQueryService internationalStandarQueryService) {
        this.internationalStandarService = internationalStandarService;
        this.internationalStandarQueryService = internationalStandarQueryService;
    }

    /**
     * POST  /international-standars : Create a new internationalStandar.
     *
     * @param internationalStandar the internationalStandar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internationalStandar, or with status 400 (Bad Request) if the internationalStandar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/international-standars")
    @Timed
    public ResponseEntity<InternationalStandar> createInternationalStandar(@Valid @RequestBody InternationalStandar internationalStandar) throws URISyntaxException {
        log.debug("REST request to save InternationalStandar : {}", internationalStandar);
        if (internationalStandar.getId() != null) {
            throw new BadRequestAlertException("A new internationalStandar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternationalStandar result = internationalStandarService.save(internationalStandar);
        return ResponseEntity.created(new URI("/api/international-standars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /international-standars : Updates an existing internationalStandar.
     *
     * @param internationalStandar the internationalStandar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internationalStandar,
     * or with status 400 (Bad Request) if the internationalStandar is not valid,
     * or with status 500 (Internal Server Error) if the internationalStandar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/international-standars")
    @Timed
    public ResponseEntity<InternationalStandar> updateInternationalStandar(@Valid @RequestBody InternationalStandar internationalStandar) throws URISyntaxException {
        log.debug("REST request to update InternationalStandar : {}", internationalStandar);
        if (internationalStandar.getId() == null) {
            return createInternationalStandar(internationalStandar);
        }
        InternationalStandar result = internationalStandarService.save(internationalStandar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, internationalStandar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /international-standars : get all the internationalStandars.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of internationalStandars in body
     */
    @GetMapping("/international-standars")
    @Timed
    public ResponseEntity<List<InternationalStandar>> getAllInternationalStandars(InternationalStandarCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InternationalStandars by criteria: {}", criteria);
        Page<InternationalStandar> page = internationalStandarQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/international-standars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /international-standars/:id : get the "id" internationalStandar.
     *
     * @param id the id of the internationalStandar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internationalStandar, or with status 404 (Not Found)
     */
    @GetMapping("/international-standars/{id}")
    @Timed
    public ResponseEntity<InternationalStandar> getInternationalStandar(@PathVariable Long id) {
        log.debug("REST request to get InternationalStandar : {}", id);
        InternationalStandar internationalStandar = internationalStandarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(internationalStandar));
    }

    /**
     * DELETE  /international-standars/:id : delete the "id" internationalStandar.
     *
     * @param id the id of the internationalStandar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/international-standars/{id}")
    @Timed
    public ResponseEntity<Void> deleteInternationalStandar(@PathVariable Long id) {
        log.debug("REST request to delete InternationalStandar : {}", id);
        internationalStandarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
