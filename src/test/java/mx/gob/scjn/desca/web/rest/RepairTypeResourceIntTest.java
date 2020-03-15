package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.RepairType;
import mx.gob.scjn.desca.repository.RepairTypeRepository;
import mx.gob.scjn.desca.service.RepairTypeService;
import mx.gob.scjn.desca.repository.search.RepairTypeSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairTypeDTO;
import mx.gob.scjn.desca.service.mapper.RepairTypeMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.RepairTypeCriteria;
import mx.gob.scjn.desca.service.RepairTypeQueryService;

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
 * Test class for the RepairTypeResource REST controller.
 *
 * @see RepairTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class RepairTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private RepairTypeRepository repairTypeRepository;

    @Autowired
    private RepairTypeMapper repairTypeMapper;

    @Autowired
    private RepairTypeService repairTypeService;

    @Autowired
    private RepairTypeSearchRepository repairTypeSearchRepository;

    @Autowired
    private RepairTypeQueryService repairTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRepairTypeMockMvc;

    private RepairType repairType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RepairTypeResource repairTypeResource = new RepairTypeResource(repairTypeService, repairTypeQueryService);
        this.restRepairTypeMockMvc = MockMvcBuilders.standaloneSetup(repairTypeResource)
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
    public static RepairType createEntity(EntityManager em) {
        RepairType repairType = new RepairType()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return repairType;
    }

    @Before
    public void initTest() {
        repairTypeSearchRepository.deleteAll();
        repairType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRepairType() throws Exception {
        int databaseSizeBeforeCreate = repairTypeRepository.findAll().size();

        // Create the RepairType
        RepairTypeDTO repairTypeDTO = repairTypeMapper.toDto(repairType);
        restRepairTypeMockMvc.perform(post("/api/repair-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RepairType in the database
        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RepairType testRepairType = repairTypeList.get(repairTypeList.size() - 1);
        assertThat(testRepairType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepairType.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the RepairType in Elasticsearch
        RepairType repairTypeEs = repairTypeSearchRepository.findOne(testRepairType.getId());
        assertThat(repairTypeEs).isEqualToIgnoringGivenFields(testRepairType);
    }

    @Test
    @Transactional
    public void createRepairTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repairTypeRepository.findAll().size();

        // Create the RepairType with an existing ID
        repairType.setId(1L);
        RepairTypeDTO repairTypeDTO = repairTypeMapper.toDto(repairType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairTypeMockMvc.perform(post("/api/repair-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RepairType in the database
        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairTypeRepository.findAll().size();
        // set the field null
        repairType.setName(null);

        // Create the RepairType, which fails.
        RepairTypeDTO repairTypeDTO = repairTypeMapper.toDto(repairType);

        restRepairTypeMockMvc.perform(post("/api/repair-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairTypeDTO)))
            .andExpect(status().isBadRequest());

        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepairTypes() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList
        restRepairTypeMockMvc.perform(get("/api/repair-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRepairType() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get the repairType
        restRepairTypeMockMvc.perform(get("/api/repair-types/{id}", repairType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(repairType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRepairTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where name equals to DEFAULT_NAME
        defaultRepairTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the repairTypeList where name equals to UPDATED_NAME
        defaultRepairTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRepairTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the repairTypeList where name equals to UPDATED_NAME
        defaultRepairTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where name is not null
        defaultRepairTypeShouldBeFound("name.specified=true");

        // Get all the repairTypeList where name is null
        defaultRepairTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRepairTypesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where enabled equals to DEFAULT_ENABLED
        defaultRepairTypeShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the repairTypeList where enabled equals to UPDATED_ENABLED
        defaultRepairTypeShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRepairTypesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultRepairTypeShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the repairTypeList where enabled equals to UPDATED_ENABLED
        defaultRepairTypeShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRepairTypesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);

        // Get all the repairTypeList where enabled is not null
        defaultRepairTypeShouldBeFound("enabled.specified=true");

        // Get all the repairTypeList where enabled is null
        defaultRepairTypeShouldNotBeFound("enabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRepairTypeShouldBeFound(String filter) throws Exception {
        restRepairTypeMockMvc.perform(get("/api/repair-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRepairTypeShouldNotBeFound(String filter) throws Exception {
        restRepairTypeMockMvc.perform(get("/api/repair-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRepairType() throws Exception {
        // Get the repairType
        restRepairTypeMockMvc.perform(get("/api/repair-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepairType() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);
        repairTypeSearchRepository.save(repairType);
        int databaseSizeBeforeUpdate = repairTypeRepository.findAll().size();

        // Update the repairType
        RepairType updatedRepairType = repairTypeRepository.findOne(repairType.getId());
        // Disconnect from session so that the updates on updatedRepairType are not directly saved in db
        em.detach(updatedRepairType);
        updatedRepairType
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        RepairTypeDTO repairTypeDTO = repairTypeMapper.toDto(updatedRepairType);

        restRepairTypeMockMvc.perform(put("/api/repair-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairTypeDTO)))
            .andExpect(status().isOk());

        // Validate the RepairType in the database
        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeUpdate);
        RepairType testRepairType = repairTypeList.get(repairTypeList.size() - 1);
        assertThat(testRepairType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepairType.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the RepairType in Elasticsearch
        RepairType repairTypeEs = repairTypeSearchRepository.findOne(testRepairType.getId());
        assertThat(repairTypeEs).isEqualToIgnoringGivenFields(testRepairType);
    }

    @Test
    @Transactional
    public void updateNonExistingRepairType() throws Exception {
        int databaseSizeBeforeUpdate = repairTypeRepository.findAll().size();

        // Create the RepairType
        RepairTypeDTO repairTypeDTO = repairTypeMapper.toDto(repairType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRepairTypeMockMvc.perform(put("/api/repair-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RepairType in the database
        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRepairType() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);
        repairTypeSearchRepository.save(repairType);
        int databaseSizeBeforeDelete = repairTypeRepository.findAll().size();

        // Get the repairType
        restRepairTypeMockMvc.perform(delete("/api/repair-types/{id}", repairType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean repairTypeExistsInEs = repairTypeSearchRepository.exists(repairType.getId());
        assertThat(repairTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<RepairType> repairTypeList = repairTypeRepository.findAll();
        assertThat(repairTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRepairType() throws Exception {
        // Initialize the database
        repairTypeRepository.saveAndFlush(repairType);
        repairTypeSearchRepository.save(repairType);

        // Search the repairType
        restRepairTypeMockMvc.perform(get("/api/_search/repair-types?query=id:" + repairType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairType.class);
        RepairType repairType1 = new RepairType();
        repairType1.setId(1L);
        RepairType repairType2 = new RepairType();
        repairType2.setId(repairType1.getId());
        assertThat(repairType1).isEqualTo(repairType2);
        repairType2.setId(2L);
        assertThat(repairType1).isNotEqualTo(repairType2);
        repairType1.setId(null);
        assertThat(repairType1).isNotEqualTo(repairType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairTypeDTO.class);
        RepairTypeDTO repairTypeDTO1 = new RepairTypeDTO();
        repairTypeDTO1.setId(1L);
        RepairTypeDTO repairTypeDTO2 = new RepairTypeDTO();
        assertThat(repairTypeDTO1).isNotEqualTo(repairTypeDTO2);
        repairTypeDTO2.setId(repairTypeDTO1.getId());
        assertThat(repairTypeDTO1).isEqualTo(repairTypeDTO2);
        repairTypeDTO2.setId(2L);
        assertThat(repairTypeDTO1).isNotEqualTo(repairTypeDTO2);
        repairTypeDTO1.setId(null);
        assertThat(repairTypeDTO1).isNotEqualTo(repairTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(repairTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(repairTypeMapper.fromId(null)).isNull();
    }
}
