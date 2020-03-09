package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.DescaWayService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import mx.gob.scjn.desca.service.dto.DescaWayCriteria;
import mx.gob.scjn.desca.service.DescaWayQueryService;
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
 * REST controller for managing DescaWay.
 */
@RestController
@RequestMapping("/api")
public class DescaWayResource {

    private final Logger log = LoggerFactory.getLogger(DescaWayResource.class);

    private static final String ENTITY_NAME = "descaWay";

    private final DescaWayService descaWayService;

    private final DescaWayQueryService descaWayQueryService;

    public DescaWayResource(DescaWayService descaWayService, DescaWayQueryService descaWayQueryService) {
        this.descaWayService = descaWayService;
        this.descaWayQueryService = descaWayQueryService;
    }

    /**
     * POST  /desca-ways : Create a new descaWay.
     *
     * @param descaWayDTO the descaWayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descaWayDTO, or with status 400 (Bad Request) if the descaWay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/desca-ways")
    @Timed
    public ResponseEntity<DescaWayDTO> createDescaWay(@Valid @RequestBody DescaWayDTO descaWayDTO) throws URISyntaxException {
        log.debug("REST request to save DescaWay : {}", descaWayDTO);
        if (descaWayDTO.getId() != null) {
            throw new BadRequestAlertException("A new descaWay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescaWayDTO result = descaWayService.save(descaWayDTO);
        return ResponseEntity.created(new URI("/api/desca-ways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /desca-ways : Updates an existing descaWay.
     *
     * @param descaWayDTO the descaWayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descaWayDTO,
     * or with status 400 (Bad Request) if the descaWayDTO is not valid,
     * or with status 500 (Internal Server Error) if the descaWayDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/desca-ways")
    @Timed
    public ResponseEntity<DescaWayDTO> updateDescaWay(@Valid @RequestBody DescaWayDTO descaWayDTO) throws URISyntaxException {
        log.debug("REST request to update DescaWay : {}", descaWayDTO);
        if (descaWayDTO.getId() == null) {
            return createDescaWay(descaWayDTO);
        }
        DescaWayDTO result = descaWayService.save(descaWayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descaWayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /desca-ways : get all the descaWays.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of descaWays in body
     */
    @GetMapping("/desca-ways")
    @Timed
    public ResponseEntity<List<DescaWayDTO>> getAllDescaWays(DescaWayCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DescaWays by criteria: {}", criteria);
        Page<DescaWayDTO> page = descaWayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/desca-ways");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /desca-ways/:id : get the "id" descaWay.
     *
     * @param id the id of the descaWayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descaWayDTO, or with status 404 (Not Found)
     */
    @GetMapping("/desca-ways/{id}")
    @Timed
    public ResponseEntity<DescaWayDTO> getDescaWay(@PathVariable Long id) {
        log.debug("REST request to get DescaWay : {}", id);
        DescaWayDTO descaWayDTO = descaWayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descaWayDTO));
    }

    /**
     * DELETE  /desca-ways/:id : delete the "id" descaWay.
     *
     * @param id the id of the descaWayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/desca-ways/{id}")
    @Timed
    public ResponseEntity<Void> deleteDescaWay(@PathVariable Long id) {
        log.debug("REST request to delete DescaWay : {}", id);
        descaWayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
