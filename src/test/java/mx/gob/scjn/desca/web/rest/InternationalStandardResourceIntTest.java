package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.InternationalStandard;
import mx.gob.scjn.desca.repository.InternationalStandardRepository;
import mx.gob.scjn.desca.service.InternationalStandardService;
import mx.gob.scjn.desca.repository.search.InternationalStandardSearchRepository;
import mx.gob.scjn.desca.service.dto.InternationalStandardDTO;
import mx.gob.scjn.desca.service.mapper.InternationalStandardMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.InternationalStandardCriteria;
import mx.gob.scjn.desca.service.InternationalStandardQueryService;

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
 * Test class for the InternationalStandardResource REST controller.
 *
 * @see InternationalStandardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class InternationalStandardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private InternationalStandardRepository internationalStandardRepository;

    @Autowired
    private InternationalStandardMapper internationalStandardMapper;

    @Autowired
    private InternationalStandardService internationalStandardService;

    @Autowired
    private InternationalStandardSearchRepository internationalStandardSearchRepository;

    @Autowired
    private InternationalStandardQueryService internationalStandardQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternationalStandardMockMvc;

    private InternationalStandard internationalStandard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternationalStandardResource internationalStandardResource = new InternationalStandardResource(internationalStandardService, internationalStandardQueryService);
        this.restInternationalStandardMockMvc = MockMvcBuilders.standaloneSetup(internationalStandardResource)
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
    public static InternationalStandard createEntity(EntityManager em) {
        InternationalStandard internationalStandard = new InternationalStandard()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return internationalStandard;
    }

    @Before
    public void initTest() {
        internationalStandardSearchRepository.deleteAll();
        internationalStandard = createEntity(em);
    }

    @Test
    @Transactional
    public void createInternationalStandard() throws Exception {
        int databaseSizeBeforeCreate = internationalStandardRepository.findAll().size();

        // Create the InternationalStandard
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(internationalStandard);
        restInternationalStandardMockMvc.perform(post("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isCreated());

        // Validate the InternationalStandard in the database
        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeCreate + 1);
        InternationalStandard testInternationalStandard = internationalStandardList.get(internationalStandardList.size() - 1);
        assertThat(testInternationalStandard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInternationalStandard.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the InternationalStandard in Elasticsearch
        InternationalStandard internationalStandardEs = internationalStandardSearchRepository.findOne(testInternationalStandard.getId());
        assertThat(internationalStandardEs).isEqualToIgnoringGivenFields(testInternationalStandard);
    }

    @Test
    @Transactional
    public void createInternationalStandardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internationalStandardRepository.findAll().size();

        // Create the InternationalStandard with an existing ID
        internationalStandard.setId(1L);
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(internationalStandard);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternationalStandardMockMvc.perform(post("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InternationalStandard in the database
        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = internationalStandardRepository.findAll().size();
        // set the field null
        internationalStandard.setName(null);

        // Create the InternationalStandard, which fails.
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(internationalStandard);

        restInternationalStandardMockMvc.perform(post("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isBadRequest());

        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = internationalStandardRepository.findAll().size();
        // set the field null
        internationalStandard.setStatus(null);

        // Create the InternationalStandard, which fails.
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(internationalStandard);

        restInternationalStandardMockMvc.perform(post("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isBadRequest());

        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternationalStandards() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList
        restInternationalStandardMockMvc.perform(get("/api/international-standards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalStandard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getInternationalStandard() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get the internationalStandard
        restInternationalStandardMockMvc.perform(get("/api/international-standards/{id}", internationalStandard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internationalStandard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where name equals to DEFAULT_NAME
        defaultInternationalStandardShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the internationalStandardList where name equals to UPDATED_NAME
        defaultInternationalStandardShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInternationalStandardShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the internationalStandardList where name equals to UPDATED_NAME
        defaultInternationalStandardShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where name is not null
        defaultInternationalStandardShouldBeFound("name.specified=true");

        // Get all the internationalStandardList where name is null
        defaultInternationalStandardShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where status equals to DEFAULT_STATUS
        defaultInternationalStandardShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the internationalStandardList where status equals to UPDATED_STATUS
        defaultInternationalStandardShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInternationalStandardShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the internationalStandardList where status equals to UPDATED_STATUS
        defaultInternationalStandardShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInternationalStandardsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);

        // Get all the internationalStandardList where status is not null
        defaultInternationalStandardShouldBeFound("status.specified=true");

        // Get all the internationalStandardList where status is null
        defaultInternationalStandardShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInternationalStandardShouldBeFound(String filter) throws Exception {
        restInternationalStandardMockMvc.perform(get("/api/international-standards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalStandard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInternationalStandardShouldNotBeFound(String filter) throws Exception {
        restInternationalStandardMockMvc.perform(get("/api/international-standards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInternationalStandard() throws Exception {
        // Get the internationalStandard
        restInternationalStandardMockMvc.perform(get("/api/international-standards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternationalStandard() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);
        internationalStandardSearchRepository.save(internationalStandard);
        int databaseSizeBeforeUpdate = internationalStandardRepository.findAll().size();

        // Update the internationalStandard
        InternationalStandard updatedInternationalStandard = internationalStandardRepository.findOne(internationalStandard.getId());
        // Disconnect from session so that the updates on updatedInternationalStandard are not directly saved in db
        em.detach(updatedInternationalStandard);
        updatedInternationalStandard
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(updatedInternationalStandard);

        restInternationalStandardMockMvc.perform(put("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isOk());

        // Validate the InternationalStandard in the database
        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeUpdate);
        InternationalStandard testInternationalStandard = internationalStandardList.get(internationalStandardList.size() - 1);
        assertThat(testInternationalStandard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInternationalStandard.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the InternationalStandard in Elasticsearch
        InternationalStandard internationalStandardEs = internationalStandardSearchRepository.findOne(testInternationalStandard.getId());
        assertThat(internationalStandardEs).isEqualToIgnoringGivenFields(testInternationalStandard);
    }

    @Test
    @Transactional
    public void updateNonExistingInternationalStandard() throws Exception {
        int databaseSizeBeforeUpdate = internationalStandardRepository.findAll().size();

        // Create the InternationalStandard
        InternationalStandardDTO internationalStandardDTO = internationalStandardMapper.toDto(internationalStandard);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternationalStandardMockMvc.perform(put("/api/international-standards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandardDTO)))
            .andExpect(status().isCreated());

        // Validate the InternationalStandard in the database
        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInternationalStandard() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);
        internationalStandardSearchRepository.save(internationalStandard);
        int databaseSizeBeforeDelete = internationalStandardRepository.findAll().size();

        // Get the internationalStandard
        restInternationalStandardMockMvc.perform(delete("/api/international-standards/{id}", internationalStandard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean internationalStandardExistsInEs = internationalStandardSearchRepository.exists(internationalStandard.getId());
        assertThat(internationalStandardExistsInEs).isFalse();

        // Validate the database is empty
        List<InternationalStandard> internationalStandardList = internationalStandardRepository.findAll();
        assertThat(internationalStandardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternationalStandard() throws Exception {
        // Initialize the database
        internationalStandardRepository.saveAndFlush(internationalStandard);
        internationalStandardSearchRepository.save(internationalStandard);

        // Search the internationalStandard
        restInternationalStandardMockMvc.perform(get("/api/_search/international-standards?query=id:" + internationalStandard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalStandard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternationalStandard.class);
        InternationalStandard internationalStandard1 = new InternationalStandard();
        internationalStandard1.setId(1L);
        InternationalStandard internationalStandard2 = new InternationalStandard();
        internationalStandard2.setId(internationalStandard1.getId());
        assertThat(internationalStandard1).isEqualTo(internationalStandard2);
        internationalStandard2.setId(2L);
        assertThat(internationalStandard1).isNotEqualTo(internationalStandard2);
        internationalStandard1.setId(null);
        assertThat(internationalStandard1).isNotEqualTo(internationalStandard2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternationalStandardDTO.class);
        InternationalStandardDTO internationalStandardDTO1 = new InternationalStandardDTO();
        internationalStandardDTO1.setId(1L);
        InternationalStandardDTO internationalStandardDTO2 = new InternationalStandardDTO();
        assertThat(internationalStandardDTO1).isNotEqualTo(internationalStandardDTO2);
        internationalStandardDTO2.setId(internationalStandardDTO1.getId());
        assertThat(internationalStandardDTO1).isEqualTo(internationalStandardDTO2);
        internationalStandardDTO2.setId(2L);
        assertThat(internationalStandardDTO1).isNotEqualTo(internationalStandardDTO2);
        internationalStandardDTO1.setId(null);
        assertThat(internationalStandardDTO1).isNotEqualTo(internationalStandardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(internationalStandardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(internationalStandardMapper.fromId(null)).isNull();
    }
}
