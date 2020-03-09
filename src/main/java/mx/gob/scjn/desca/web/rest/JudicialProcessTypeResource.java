package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.JudicialProcessTypeService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeCriteria;
import mx.gob.scjn.desca.service.JudicialProcessTypeQueryService;
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
 * REST controller for managing JudicialProcessType.
 */
@RestController
@RequestMapping("/api")
public class JudicialProcessTypeResource {

    private final Logger log = LoggerFactory.getLogger(JudicialProcessTypeResource.class);

    private static final String ENTITY_NAME = "judicialProcessType";

    private final JudicialProcessTypeService judicialProcessTypeService;

    private final JudicialProcessTypeQueryService judicialProcessTypeQueryService;

    public JudicialProcessTypeResource(JudicialProcessTypeService judicialProcessTypeService, JudicialProcessTypeQueryService judicialProcessTypeQueryService) {
        this.judicialProcessTypeService = judicialProcessTypeService;
        this.judicialProcessTypeQueryService = judicialProcessTypeQueryService;
    }

    /**
     * POST  /judicial-process-types : Create a new judicialProcessType.
     *
     * @param judicialProcessTypeDTO the judicialProcessTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new judicialProcessTypeDTO, or with status 400 (Bad Request) if the judicialProcessType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/judicial-process-types")
    @Timed
    public ResponseEntity<JudicialProcessTypeDTO> createJudicialProcessType(@Valid @RequestBody JudicialProcessTypeDTO judicialProcessTypeDTO) throws URISyntaxException {
        log.debug("REST request to save JudicialProcessType : {}", judicialProcessTypeDTO);
        if (judicialProcessTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new judicialProcessType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JudicialProcessTypeDTO result = judicialProcessTypeService.save(judicialProcessTypeDTO);
        return ResponseEntity.created(new URI("/api/judicial-process-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /judicial-process-types : Updates an existing judicialProcessType.
     *
     * @param judicialProcessTypeDTO the judicialProcessTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated judicialProcessTypeDTO,
     * or with status 400 (Bad Request) if the judicialProcessTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the judicialProcessTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/judicial-process-types")
    @Timed
    public ResponseEntity<JudicialProcessTypeDTO> updateJudicialProcessType(@Valid @RequestBody JudicialProcessTypeDTO judicialProcessTypeDTO) throws URISyntaxException {
        log.debug("REST request to update JudicialProcessType : {}", judicialProcessTypeDTO);
        if (judicialProcessTypeDTO.getId() == null) {
            return createJudicialProcessType(judicialProcessTypeDTO);
        }
        JudicialProcessTypeDTO result = judicialProcessTypeService.save(judicialProcessTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, judicialProcessTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /judicial-process-types : get all the judicialProcessTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of judicialProcessTypes in body
     */
    @GetMapping("/judicial-process-types")
    @Timed
    public ResponseEntity<List<JudicialProcessTypeDTO>> getAllJudicialProcessTypes(JudicialProcessTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JudicialProcessTypes by criteria: {}", criteria);
        Page<JudicialProcessTypeDTO> page = judicialProcessTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/judicial-process-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /judicial-process-types/:id : get the "id" judicialProcessType.
     *
     * @param id the id of the judicialProcessTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the judicialProcessTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/judicial-process-types/{id}")
    @Timed
    public ResponseEntity<JudicialProcessTypeDTO> getJudicialProcessType(@PathVariable Long id) {
        log.debug("REST request to get JudicialProcessType : {}", id);
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(judicialProcessTypeDTO));
    }

    /**
     * DELETE  /judicial-process-types/:id : delete the "id" judicialProcessType.
     *
     * @param id the id of the judicialProcessTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/judicial-process-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteJudicialProcessType(@PathVariable Long id) {
        log.debug("REST request to delete JudicialProcessType : {}", id);
        judicialProcessTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
