package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.Institution;
import mx.gob.scjn.desca.repository.InstitutionRepository;
import mx.gob.scjn.desca.service.InstitutionService;
import mx.gob.scjn.desca.repository.search.InstitutionSearchRepository;
import mx.gob.scjn.desca.service.dto.InstitutionDTO;
import mx.gob.scjn.desca.service.mapper.InstitutionMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.InstitutionCriteria;
import mx.gob.scjn.desca.service.InstitutionQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static mx.gob.scjn.desca.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InstitutionResource REST controller.
 *
 * @see InstitutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class InstitutionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private InstitutionSearchRepository institutionSearchRepository;

    @Autowired
    private InstitutionQueryService institutionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInstitutionMockMvc;

    private Institution institution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstitutionResource institutionResource = new InstitutionResource(institutionService, institutionQueryService);
        this.restInstitutionMockMvc = MockMvcBuilders.standaloneSetup(institutionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institution createEntity(EntityManager em) {
        Institution institution = new Institution()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return institution;
    }

    @Before
    public void initTest() {
        institutionSearchRepository.deleteAll();
        institution = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstitution() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().size();

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);
        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate + 1);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstitution.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the Institution in Elasticsearch
        Institution institutionEs = institutionSearchRepository.findOne(testInstitution.getId());
        assertThat(institutionEs).isEqualToIgnoringGivenFields(testInstitution);
    }

    @Test
    @Transactional
    public void createInstitutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().size();

        // Create the Institution with an existing ID
        institution.setId(1L);
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setName(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstitutions() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList
        restInstitutionMockMvc.perform(get("/api/institutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institution.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get the institution
        restInstitutionMockMvc.perform(get("/api/institutions/{id}", institution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(institution.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllInstitutionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where name equals to DEFAULT_NAME
        defaultInstitutionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the institutionList where name equals to UPDATED_NAME
        defaultInstitutionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInstitutionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the institutionList where name equals to UPDATED_NAME
        defaultInstitutionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where name is not null
        defaultInstitutionShouldBeFound("name.specified=true");

        // Get all the institutionList where name is null
        defaultInstitutionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllInstitutionsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where enabled equals to DEFAULT_ENABLED
        defaultInstitutionShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the institutionList where enabled equals to UPDATED_ENABLED
        defaultInstitutionShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllInstitutionsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultInstitutionShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the institutionList where enabled equals to UPDATED_ENABLED
        defaultInstitutionShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllInstitutionsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList where enabled is not null
        defaultInstitutionShouldBeFound("enabled.specified=true");

        // Get all the institutionList where enabled is null
        defaultInstitutionShouldNotBeFound("enabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInstitutionShouldBeFound(String filter) throws Exception {
        restInstitutionMockMvc.perform(get("/api/institutions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institution.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInstitutionShouldNotBeFound(String filter) throws Exception {
        restInstitutionMockMvc.perform(get("/api/institutions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInstitution() throws Exception {
        // Get the institution
        restInstitutionMockMvc.perform(get("/api/institutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);
        institutionSearchRepository.save(institution);
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Update the institution
        Institution updatedInstitution = institutionRepository.findOne(institution.getId());
        // Disconnect from session so that the updates on updatedInstitution are not directly saved in db
        em.detach(updatedInstitution);
        updatedInstitution
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        InstitutionDTO institutionDTO = institutionMapper.toDto(updatedInstitution);

        restInstitutionMockMvc.perform(put("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isOk());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstitution.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the Institution in Elasticsearch
        Institution institutionEs = institutionSearchRepository.findOne(testInstitution.getId());
        assertThat(institutionEs).isEqualToIgnoringGivenFields(testInstitution);
    }

    @Test
    @Transactional
    public void updateNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstitutionMockMvc.perform(put("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);
        institutionSearchRepository.save(institution);
        int databaseSizeBeforeDelete = institutionRepository.findAll().size();

        // Get the institution
        restInstitutionMockMvc.perform(delete("/api/institutions/{id}", institution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean institutionExistsInEs = institutionSearchRepository.exists(institution.getId());
        assertThat(institutionExistsInEs).isFalse();

        // Validate the database is empty
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);
        institutionSearchRepository.save(institution);

        // Search the institution
        restInstitutionMockMvc.perform(get("/api/_search/institutions?query=id:" + institution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institution.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institution.class);
        Institution institution1 = new Institution();
        institution1.setId(1L);
        Institution institution2 = new Institution();
        institution2.setId(institution1.getId());
        assertThat(institution1).isEqualTo(institution2);
        institution2.setId(2L);
        assertThat(institution1).isNotEqualTo(institution2);
        institution1.setId(null);
        assertThat(institution1).isNotEqualTo(institution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionDTO.class);
        InstitutionDTO institutionDTO1 = new InstitutionDTO();
        institutionDTO1.setId(1L);
        InstitutionDTO institutionDTO2 = new InstitutionDTO();
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO2.setId(institutionDTO1.getId());
        assertThat(institutionDTO1).isEqualTo(institutionDTO2);
        institutionDTO2.setId(2L);
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO1.setId(null);
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(institutionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(institutionMapper.fromId(null)).isNull();
    }
}
