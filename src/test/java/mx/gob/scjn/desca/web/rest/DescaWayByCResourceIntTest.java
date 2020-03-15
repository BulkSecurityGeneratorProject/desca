package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.DescaWayByC;
import mx.gob.scjn.desca.repository.DescaWayByCRepository;
import mx.gob.scjn.desca.service.DescaWayByCService;
import mx.gob.scjn.desca.repository.search.DescaWayByCSearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayByCDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayByCMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.DescaWayByCCriteria;
import mx.gob.scjn.desca.service.DescaWayByCQueryService;

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
 * Test class for the DescaWayByCResource REST controller.
 *
 * @see DescaWayByCResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class DescaWayByCResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private DescaWayByCRepository descaWayByCRepository;

    @Autowired
    private DescaWayByCMapper descaWayByCMapper;

    @Autowired
    private DescaWayByCService descaWayByCService;

    @Autowired
    private DescaWayByCSearchRepository descaWayByCSearchRepository;

    @Autowired
    private DescaWayByCQueryService descaWayByCQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescaWayByCMockMvc;

    private DescaWayByC descaWayByC;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescaWayByCResource descaWayByCResource = new DescaWayByCResource(descaWayByCService, descaWayByCQueryService);
        this.restDescaWayByCMockMvc = MockMvcBuilders.standaloneSetup(descaWayByCResource)
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
    public static DescaWayByC createEntity(EntityManager em) {
        DescaWayByC descaWayByC = new DescaWayByC()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return descaWayByC;
    }

    @Before
    public void initTest() {
        descaWayByCSearchRepository.deleteAll();
        descaWayByC = createEntity(em);
    }

    @Test
    @Transactional
    public void createDescaWayByC() throws Exception {
        int databaseSizeBeforeCreate = descaWayByCRepository.findAll().size();

        // Create the DescaWayByC
        DescaWayByCDTO descaWayByCDTO = descaWayByCMapper.toDto(descaWayByC);
        restDescaWayByCMockMvc.perform(post("/api/desca-way-by-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayByCDTO)))
            .andExpect(status().isCreated());

        // Validate the DescaWayByC in the database
        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeCreate + 1);
        DescaWayByC testDescaWayByC = descaWayByCList.get(descaWayByCList.size() - 1);
        assertThat(testDescaWayByC.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDescaWayByC.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the DescaWayByC in Elasticsearch
        DescaWayByC descaWayByCEs = descaWayByCSearchRepository.findOne(testDescaWayByC.getId());
        assertThat(descaWayByCEs).isEqualToIgnoringGivenFields(testDescaWayByC);
    }

    @Test
    @Transactional
    public void createDescaWayByCWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descaWayByCRepository.findAll().size();

        // Create the DescaWayByC with an existing ID
        descaWayByC.setId(1L);
        DescaWayByCDTO descaWayByCDTO = descaWayByCMapper.toDto(descaWayByC);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescaWayByCMockMvc.perform(post("/api/desca-way-by-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayByCDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DescaWayByC in the database
        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = descaWayByCRepository.findAll().size();
        // set the field null
        descaWayByC.setName(null);

        // Create the DescaWayByC, which fails.
        DescaWayByCDTO descaWayByCDTO = descaWayByCMapper.toDto(descaWayByC);

        restDescaWayByCMockMvc.perform(post("/api/desca-way-by-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayByCDTO)))
            .andExpect(status().isBadRequest());

        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDescaWayByCS() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList
        restDescaWayByCMockMvc.perform(get("/api/desca-way-by-cs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWayByC.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getDescaWayByC() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get the descaWayByC
        restDescaWayByCMockMvc.perform(get("/api/desca-way-by-cs/{id}", descaWayByC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descaWayByC.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where name equals to DEFAULT_NAME
        defaultDescaWayByCShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the descaWayByCList where name equals to UPDATED_NAME
        defaultDescaWayByCShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByNameIsInShouldWork() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDescaWayByCShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the descaWayByCList where name equals to UPDATED_NAME
        defaultDescaWayByCShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where name is not null
        defaultDescaWayByCShouldBeFound("name.specified=true");

        // Get all the descaWayByCList where name is null
        defaultDescaWayByCShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where enabled equals to DEFAULT_ENABLED
        defaultDescaWayByCShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the descaWayByCList where enabled equals to UPDATED_ENABLED
        defaultDescaWayByCShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultDescaWayByCShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the descaWayByCList where enabled equals to UPDATED_ENABLED
        defaultDescaWayByCShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllDescaWayByCSByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);

        // Get all the descaWayByCList where enabled is not null
        defaultDescaWayByCShouldBeFound("enabled.specified=true");

        // Get all the descaWayByCList where enabled is null
        defaultDescaWayByCShouldNotBeFound("enabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDescaWayByCShouldBeFound(String filter) throws Exception {
        restDescaWayByCMockMvc.perform(get("/api/desca-way-by-cs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWayByC.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDescaWayByCShouldNotBeFound(String filter) throws Exception {
        restDescaWayByCMockMvc.perform(get("/api/desca-way-by-cs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDescaWayByC() throws Exception {
        // Get the descaWayByC
        restDescaWayByCMockMvc.perform(get("/api/desca-way-by-cs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDescaWayByC() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);
        descaWayByCSearchRepository.save(descaWayByC);
        int databaseSizeBeforeUpdate = descaWayByCRepository.findAll().size();

        // Update the descaWayByC
        DescaWayByC updatedDescaWayByC = descaWayByCRepository.findOne(descaWayByC.getId());
        // Disconnect from session so that the updates on updatedDescaWayByC are not directly saved in db
        em.detach(updatedDescaWayByC);
        updatedDescaWayByC
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        DescaWayByCDTO descaWayByCDTO = descaWayByCMapper.toDto(updatedDescaWayByC);

        restDescaWayByCMockMvc.perform(put("/api/desca-way-by-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayByCDTO)))
            .andExpect(status().isOk());

        // Validate the DescaWayByC in the database
        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeUpdate);
        DescaWayByC testDescaWayByC = descaWayByCList.get(descaWayByCList.size() - 1);
        assertThat(testDescaWayByC.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDescaWayByC.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the DescaWayByC in Elasticsearch
        DescaWayByC descaWayByCEs = descaWayByCSearchRepository.findOne(testDescaWayByC.getId());
        assertThat(descaWayByCEs).isEqualToIgnoringGivenFields(testDescaWayByC);
    }

    @Test
    @Transactional
    public void updateNonExistingDescaWayByC() throws Exception {
        int databaseSizeBeforeUpdate = descaWayByCRepository.findAll().size();

        // Create the DescaWayByC
        DescaWayByCDTO descaWayByCDTO = descaWayByCMapper.toDto(descaWayByC);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescaWayByCMockMvc.perform(put("/api/desca-way-by-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayByCDTO)))
            .andExpect(status().isCreated());

        // Validate the DescaWayByC in the database
        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDescaWayByC() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);
        descaWayByCSearchRepository.save(descaWayByC);
        int databaseSizeBeforeDelete = descaWayByCRepository.findAll().size();

        // Get the descaWayByC
        restDescaWayByCMockMvc.perform(delete("/api/desca-way-by-cs/{id}", descaWayByC.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean descaWayByCExistsInEs = descaWayByCSearchRepository.exists(descaWayByC.getId());
        assertThat(descaWayByCExistsInEs).isFalse();

        // Validate the database is empty
        List<DescaWayByC> descaWayByCList = descaWayByCRepository.findAll();
        assertThat(descaWayByCList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDescaWayByC() throws Exception {
        // Initialize the database
        descaWayByCRepository.saveAndFlush(descaWayByC);
        descaWayByCSearchRepository.save(descaWayByC);

        // Search the descaWayByC
        restDescaWayByCMockMvc.perform(get("/api/_search/desca-way-by-cs?query=id:" + descaWayByC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWayByC.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescaWayByC.class);
        DescaWayByC descaWayByC1 = new DescaWayByC();
        descaWayByC1.setId(1L);
        DescaWayByC descaWayByC2 = new DescaWayByC();
        descaWayByC2.setId(descaWayByC1.getId());
        assertThat(descaWayByC1).isEqualTo(descaWayByC2);
        descaWayByC2.setId(2L);
        assertThat(descaWayByC1).isNotEqualTo(descaWayByC2);
        descaWayByC1.setId(null);
        assertThat(descaWayByC1).isNotEqualTo(descaWayByC2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescaWayByCDTO.class);
        DescaWayByCDTO descaWayByCDTO1 = new DescaWayByCDTO();
        descaWayByCDTO1.setId(1L);
        DescaWayByCDTO descaWayByCDTO2 = new DescaWayByCDTO();
        assertThat(descaWayByCDTO1).isNotEqualTo(descaWayByCDTO2);
        descaWayByCDTO2.setId(descaWayByCDTO1.getId());
        assertThat(descaWayByCDTO1).isEqualTo(descaWayByCDTO2);
        descaWayByCDTO2.setId(2L);
        assertThat(descaWayByCDTO1).isNotEqualTo(descaWayByCDTO2);
        descaWayByCDTO1.setId(null);
        assertThat(descaWayByCDTO1).isNotEqualTo(descaWayByCDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(descaWayByCMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(descaWayByCMapper.fromId(null)).isNull();
    }
}
