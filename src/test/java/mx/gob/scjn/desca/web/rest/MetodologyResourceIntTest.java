package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.Metodology;
import mx.gob.scjn.desca.repository.MetodologyRepository;
import mx.gob.scjn.desca.service.MetodologyService;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.MetodologyCriteria;
import mx.gob.scjn.desca.service.MetodologyQueryService;

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
 * Test class for the MetodologyResource REST controller.
 *
 * @see MetodologyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class MetodologyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private MetodologyRepository metodologyRepository;

    @Autowired
    private MetodologyService metodologyService;

    @Autowired
    private MetodologyQueryService metodologyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetodologyMockMvc;

    private Metodology metodology;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetodologyResource metodologyResource = new MetodologyResource(metodologyService, metodologyQueryService);
        this.restMetodologyMockMvc = MockMvcBuilders.standaloneSetup(metodologyResource)
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
    public static Metodology createEntity(EntityManager em) {
        Metodology metodology = new Metodology()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return metodology;
    }

    @Before
    public void initTest() {
        metodology = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetodology() throws Exception {
        int databaseSizeBeforeCreate = metodologyRepository.findAll().size();

        // Create the Metodology
        restMetodologyMockMvc.perform(post("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isCreated());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeCreate + 1);
        Metodology testMetodology = metodologyList.get(metodologyList.size() - 1);
        assertThat(testMetodology.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMetodology.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createMetodologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metodologyRepository.findAll().size();

        // Create the Metodology with an existing ID
        metodology.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetodologyMockMvc.perform(post("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isBadRequest());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metodologyRepository.findAll().size();
        // set the field null
        metodology.setName(null);

        // Create the Metodology, which fails.

        restMetodologyMockMvc.perform(post("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isBadRequest());

        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetodologies() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList
        restMetodologyMockMvc.perform(get("/api/metodologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMetodology() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get the metodology
        restMetodologyMockMvc.perform(get("/api/metodologies/{id}", metodology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metodology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMetodologiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where name equals to DEFAULT_NAME
        defaultMetodologyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the metodologyList where name equals to UPDATED_NAME
        defaultMetodologyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMetodologiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMetodologyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the metodologyList where name equals to UPDATED_NAME
        defaultMetodologyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMetodologiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where name is not null
        defaultMetodologyShouldBeFound("name.specified=true");

        // Get all the metodologyList where name is null
        defaultMetodologyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMetodologiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where status equals to DEFAULT_STATUS
        defaultMetodologyShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the metodologyList where status equals to UPDATED_STATUS
        defaultMetodologyShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMetodologiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMetodologyShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the metodologyList where status equals to UPDATED_STATUS
        defaultMetodologyShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMetodologiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList where status is not null
        defaultMetodologyShouldBeFound("status.specified=true");

        // Get all the metodologyList where status is null
        defaultMetodologyShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMetodologyShouldBeFound(String filter) throws Exception {
        restMetodologyMockMvc.perform(get("/api/metodologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMetodologyShouldNotBeFound(String filter) throws Exception {
        restMetodologyMockMvc.perform(get("/api/metodologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMetodology() throws Exception {
        // Get the metodology
        restMetodologyMockMvc.perform(get("/api/metodologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetodology() throws Exception {
        // Initialize the database
        metodologyService.save(metodology);

        int databaseSizeBeforeUpdate = metodologyRepository.findAll().size();

        // Update the metodology
        Metodology updatedMetodology = metodologyRepository.findOne(metodology.getId());
        // Disconnect from session so that the updates on updatedMetodology are not directly saved in db
        em.detach(updatedMetodology);
        updatedMetodology
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);

        restMetodologyMockMvc.perform(put("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetodology)))
            .andExpect(status().isOk());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeUpdate);
        Metodology testMetodology = metodologyList.get(metodologyList.size() - 1);
        assertThat(testMetodology.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMetodology.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingMetodology() throws Exception {
        int databaseSizeBeforeUpdate = metodologyRepository.findAll().size();

        // Create the Metodology

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetodologyMockMvc.perform(put("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isCreated());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetodology() throws Exception {
        // Initialize the database
        metodologyService.save(metodology);

        int databaseSizeBeforeDelete = metodologyRepository.findAll().size();

        // Get the metodology
        restMetodologyMockMvc.perform(delete("/api/metodologies/{id}", metodology.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metodology.class);
        Metodology metodology1 = new Metodology();
        metodology1.setId(1L);
        Metodology metodology2 = new Metodology();
        metodology2.setId(metodology1.getId());
        assertThat(metodology1).isEqualTo(metodology2);
        metodology2.setId(2L);
        assertThat(metodology1).isNotEqualTo(metodology2);
        metodology1.setId(null);
        assertThat(metodology1).isNotEqualTo(metodology2);
    }
}
