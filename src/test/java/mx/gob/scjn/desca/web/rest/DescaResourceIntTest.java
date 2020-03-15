package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.Desca;
import mx.gob.scjn.desca.repository.DescaRepository;
import mx.gob.scjn.desca.service.DescaService;
import mx.gob.scjn.desca.repository.search.DescaSearchRepository;
import mx.gob.scjn.desca.service.dto.DescaDTO;
import mx.gob.scjn.desca.service.mapper.DescaMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.DescaCriteria;
import mx.gob.scjn.desca.service.DescaQueryService;

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
 * Test class for the DescaResource REST controller.
 *
 * @see DescaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class DescaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private DescaRepository descaRepository;

    @Autowired
    private DescaMapper descaMapper;

    @Autowired
    private DescaService descaService;

    @Autowired
    private DescaSearchRepository descaSearchRepository;

    @Autowired
    private DescaQueryService descaQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescaMockMvc;

    private Desca desca;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescaResource descaResource = new DescaResource(descaService, descaQueryService);
        this.restDescaMockMvc = MockMvcBuilders.standaloneSetup(descaResource)
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
    public static Desca createEntity(EntityManager em) {
        Desca desca = new Desca()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return desca;
    }

    @Before
    public void initTest() {
        descaSearchRepository.deleteAll();
        desca = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesca() throws Exception {
        int databaseSizeBeforeCreate = descaRepository.findAll().size();

        // Create the Desca
        DescaDTO descaDTO = descaMapper.toDto(desca);
        restDescaMockMvc.perform(post("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isCreated());

        // Validate the Desca in the database
        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeCreate + 1);
        Desca testDesca = descaList.get(descaList.size() - 1);
        assertThat(testDesca.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDesca.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Desca in Elasticsearch
        Desca descaEs = descaSearchRepository.findOne(testDesca.getId());
        assertThat(descaEs).isEqualToIgnoringGivenFields(testDesca);
    }

    @Test
    @Transactional
    public void createDescaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descaRepository.findAll().size();

        // Create the Desca with an existing ID
        desca.setId(1L);
        DescaDTO descaDTO = descaMapper.toDto(desca);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescaMockMvc.perform(post("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Desca in the database
        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = descaRepository.findAll().size();
        // set the field null
        desca.setName(null);

        // Create the Desca, which fails.
        DescaDTO descaDTO = descaMapper.toDto(desca);

        restDescaMockMvc.perform(post("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isBadRequest());

        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = descaRepository.findAll().size();
        // set the field null
        desca.setStatus(null);

        // Create the Desca, which fails.
        DescaDTO descaDTO = descaMapper.toDto(desca);

        restDescaMockMvc.perform(post("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isBadRequest());

        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDescas() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList
        restDescaMockMvc.perform(get("/api/descas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desca.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getDesca() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get the desca
        restDescaMockMvc.perform(get("/api/descas/{id}", desca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(desca.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDescasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where name equals to DEFAULT_NAME
        defaultDescaShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the descaList where name equals to UPDATED_NAME
        defaultDescaShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDescaShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the descaList where name equals to UPDATED_NAME
        defaultDescaShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where name is not null
        defaultDescaShouldBeFound("name.specified=true");

        // Get all the descaList where name is null
        defaultDescaShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDescasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where status equals to DEFAULT_STATUS
        defaultDescaShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the descaList where status equals to UPDATED_STATUS
        defaultDescaShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDescasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDescaShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the descaList where status equals to UPDATED_STATUS
        defaultDescaShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDescasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);

        // Get all the descaList where status is not null
        defaultDescaShouldBeFound("status.specified=true");

        // Get all the descaList where status is null
        defaultDescaShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDescaShouldBeFound(String filter) throws Exception {
        restDescaMockMvc.perform(get("/api/descas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desca.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDescaShouldNotBeFound(String filter) throws Exception {
        restDescaMockMvc.perform(get("/api/descas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDesca() throws Exception {
        // Get the desca
        restDescaMockMvc.perform(get("/api/descas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesca() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);
        descaSearchRepository.save(desca);
        int databaseSizeBeforeUpdate = descaRepository.findAll().size();

        // Update the desca
        Desca updatedDesca = descaRepository.findOne(desca.getId());
        // Disconnect from session so that the updates on updatedDesca are not directly saved in db
        em.detach(updatedDesca);
        updatedDesca
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        DescaDTO descaDTO = descaMapper.toDto(updatedDesca);

        restDescaMockMvc.perform(put("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isOk());

        // Validate the Desca in the database
        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeUpdate);
        Desca testDesca = descaList.get(descaList.size() - 1);
        assertThat(testDesca.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesca.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Desca in Elasticsearch
        Desca descaEs = descaSearchRepository.findOne(testDesca.getId());
        assertThat(descaEs).isEqualToIgnoringGivenFields(testDesca);
    }

    @Test
    @Transactional
    public void updateNonExistingDesca() throws Exception {
        int databaseSizeBeforeUpdate = descaRepository.findAll().size();

        // Create the Desca
        DescaDTO descaDTO = descaMapper.toDto(desca);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescaMockMvc.perform(put("/api/descas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaDTO)))
            .andExpect(status().isCreated());

        // Validate the Desca in the database
        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDesca() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);
        descaSearchRepository.save(desca);
        int databaseSizeBeforeDelete = descaRepository.findAll().size();

        // Get the desca
        restDescaMockMvc.perform(delete("/api/descas/{id}", desca.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean descaExistsInEs = descaSearchRepository.exists(desca.getId());
        assertThat(descaExistsInEs).isFalse();

        // Validate the database is empty
        List<Desca> descaList = descaRepository.findAll();
        assertThat(descaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDesca() throws Exception {
        // Initialize the database
        descaRepository.saveAndFlush(desca);
        descaSearchRepository.save(desca);

        // Search the desca
        restDescaMockMvc.perform(get("/api/_search/descas?query=id:" + desca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desca.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Desca.class);
        Desca desca1 = new Desca();
        desca1.setId(1L);
        Desca desca2 = new Desca();
        desca2.setId(desca1.getId());
        assertThat(desca1).isEqualTo(desca2);
        desca2.setId(2L);
        assertThat(desca1).isNotEqualTo(desca2);
        desca1.setId(null);
        assertThat(desca1).isNotEqualTo(desca2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescaDTO.class);
        DescaDTO descaDTO1 = new DescaDTO();
        descaDTO1.setId(1L);
        DescaDTO descaDTO2 = new DescaDTO();
        assertThat(descaDTO1).isNotEqualTo(descaDTO2);
        descaDTO2.setId(descaDTO1.getId());
        assertThat(descaDTO1).isEqualTo(descaDTO2);
        descaDTO2.setId(2L);
        assertThat(descaDTO1).isNotEqualTo(descaDTO2);
        descaDTO1.setId(null);
        assertThat(descaDTO1).isNotEqualTo(descaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(descaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(descaMapper.fromId(null)).isNull();
    }
}
