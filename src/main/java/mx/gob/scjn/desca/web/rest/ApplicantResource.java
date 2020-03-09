package mx.gob.scjn.desca.web.rest;

import com.codahale.metrics.annotation.Timed;
import mx.gob.scjn.desca.service.ApplicantService;
import mx.gob.scjn.desca.web.rest.errors.BadRequestAlertException;
import mx.gob.scjn.desca.web.rest.util.HeaderUtil;
import mx.gob.scjn.desca.web.rest.util.PaginationUtil;
import mx.gob.scjn.desca.service.dto.ApplicantDTO;
import mx.gob.scjn.desca.service.dto.ApplicantCriteria;
import mx.gob.scjn.desca.service.ApplicantQueryService;
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
 * REST controller for managing Applicant.
 */
@RestController
@RequestMapping("/api")
public class ApplicantResource {

    private final Logger log = LoggerFactory.getLogger(ApplicantResource.class);

    private static final String ENTITY_NAME = "applicant";

    private final ApplicantService applicantService;

    private final ApplicantQueryService applicantQueryService;

    public ApplicantResource(ApplicantService applicantService, ApplicantQueryService applicantQueryService) {
        this.applicantService = applicantService;
        this.applicantQueryService = applicantQueryService;
    }

    /**
     * POST  /applicants : Create a new applicant.
     *
     * @param applicantDTO the applicantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicantDTO, or with status 400 (Bad Request) if the applicant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applicants")
    @Timed
    public ResponseEntity<ApplicantDTO> createApplicant(@Valid @RequestBody ApplicantDTO applicantDTO) throws URISyntaxException {
        log.debug("REST request to save Applicant : {}", applicantDTO);
        if (applicantDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicantDTO result = applicantService.save(applicantDTO);
        return ResponseEntity.created(new URI("/api/applicants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applicants : Updates an existing applicant.
     *
     * @param applicantDTO the applicantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicantDTO,
     * or with status 400 (Bad Request) if the applicantDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applicants")
    @Timed
    public ResponseEntity<ApplicantDTO> updateApplicant(@Valid @RequestBody ApplicantDTO applicantDTO) throws URISyntaxException {
        log.debug("REST request to update Applicant : {}", applicantDTO);
        if (applicantDTO.getId() == null) {
            return createApplicant(applicantDTO);
        }
        ApplicantDTO result = applicantService.save(applicantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applicants : get all the applicants.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of applicants in body
     */
    @GetMapping("/applicants")
    @Timed
    public ResponseEntity<List<ApplicantDTO>> getAllApplicants(ApplicantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Applicants by criteria: {}", criteria);
        Page<ApplicantDTO> page = applicantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applicants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /applicants/:id : get the "id" applicant.
     *
     * @param id the id of the applicantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/applicants/{id}")
    @Timed
    public ResponseEntity<ApplicantDTO> getApplicant(@PathVariable Long id) {
        log.debug("REST request to get Applicant : {}", id);
        ApplicantDTO applicantDTO = applicantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicantDTO));
    }

    /**
     * DELETE  /applicants/:id : delete the "id" applicant.
     *
     * @param id the id of the applicantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applicants/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicant(@PathVariable Long id) {
        log.debug("REST request to delete Applicant : {}", id);
        applicantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
