package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.MainDatabaseService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;
import mx.gob.scjn.desca.service.dto.MainDatabaseCriteria;
import mx.gob.scjn.desca.service.MainDatabaseQueryService;
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
 * REST controller for managing MainDatabase.
 */
@RestController
@RequestMapping("/api")
public class MainDatabaseResource {

    private final Logger log = LoggerFactory.getLogger(MainDatabaseResource.class);

    private static final String ENTITY_NAME = "mainDatabase";

    private final MainDatabaseService mainDatabaseService;

    private final MainDatabaseQueryService mainDatabaseQueryService;

    public MainDatabaseResource(MainDatabaseService mainDatabaseService, MainDatabaseQueryService mainDatabaseQueryService) {
        this.mainDatabaseService = mainDatabaseService;
        this.mainDatabaseQueryService = mainDatabaseQueryService;
    }

    /**
     * POST  /main-databases : Create a new mainDatabase.
     *
     * @param mainDatabaseDTO the mainDatabaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mainDatabaseDTO, or with status 400 (Bad Request) if the mainDatabase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/main-databases")
    @Timed
    public ResponseEntity<MainDatabaseDTO> createMainDatabase(@Valid @RequestBody MainDatabaseDTO mainDatabaseDTO) throws URISyntaxException {
        log.debug("REST request to save MainDatabase : {}", mainDatabaseDTO);
        if (mainDatabaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new mainDatabase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MainDatabaseDTO result = mainDatabaseService.save(mainDatabaseDTO);
        return ResponseEntity.created(new URI("/api/main-databases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /main-databases : Updates an existing mainDatabase.
     *
     * @param mainDatabaseDTO the mainDatabaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mainDatabaseDTO,
     * or with status 400 (Bad Request) if the mainDatabaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the mainDatabaseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/main-databases")
    @Timed
    public ResponseEntity<MainDatabaseDTO> updateMainDatabase(@Valid @RequestBody MainDatabaseDTO mainDatabaseDTO) throws URISyntaxException {
        log.debug("REST request to update MainDatabase : {}", mainDatabaseDTO);
        if (mainDatabaseDTO.getId() == null) {
            return createMainDatabase(mainDatabaseDTO);
        }
        MainDatabaseDTO result = mainDatabaseService.save(mainDatabaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mainDatabaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /main-databases : get all the mainDatabases.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mainDatabases in body
     */
    @GetMapping("/main-databases")
    @Timed
    public ResponseEntity<List<MainDatabaseDTO>> getAllMainDatabases(MainDatabaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MainDatabases by criteria: {}", criteria);
        Page<MainDatabaseDTO> page = mainDatabaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/main-databases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /main-databases/:id : get the "id" mainDatabase.
     *
     * @param id the id of the mainDatabaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mainDatabaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/main-databases/{id}")
    @Timed
    public ResponseEntity<MainDatabaseDTO> getMainDatabase(@PathVariable Long id) {
        log.debug("REST request to get MainDatabase : {}", id);
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mainDatabaseDTO));
    }

    /**
     * DELETE  /main-databases/:id : delete the "id" mainDatabase.
     *
     * @param id the id of the mainDatabaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/main-databases/{id}")
    @Timed
    public ResponseEntity<Void> deleteMainDatabase(@PathVariable Long id) {
        log.debug("REST request to delete MainDatabase : {}", id);
        mainDatabaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/main-databases?query=:query : search for the mainDatabase corresponding
     * to the query.
     *
     * @param query the query of the mainDatabase search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/main-databases")
    @Timed
    public ResponseEntity<List<MainDatabaseDTO>> searchMainDatabases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MainDatabases for query {}", query);
        Page<MainDatabaseDTO> page = mainDatabaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/main-databases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
