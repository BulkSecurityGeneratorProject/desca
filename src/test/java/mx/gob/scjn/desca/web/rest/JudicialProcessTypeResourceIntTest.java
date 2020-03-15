package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.JudicialProcessType;
import mx.gob.scjn.desca.repository.JudicialProcessTypeRepository;
import mx.gob.scjn.desca.service.JudicialProcessTypeService;
import mx.gob.scjn.desca.repository.search.JudicialProcessTypeSearchRepository;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeDTO;
import mx.gob.scjn.desca.service.mapper.JudicialProcessTypeMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.JudicialProcessTypeCriteria;
import mx.gob.scjn.desca.service.JudicialProcessTypeQueryService;

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
 * Test class for the JudicialProcessTypeResource REST controller.
 *
 * @see JudicialProcessTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class JudicialProcessTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private JudicialProcessTypeRepository judicialProcessTypeRepository;

    @Autowired
    private JudicialProcessTypeMapper judicialProcessTypeMapper;

    @Autowired
    private JudicialProcessTypeService judicialProcessTypeService;

    @Autowired
    private JudicialProcessTypeSearchRepository judicialProcessTypeSearchRepository;

    @Autowired
    private JudicialProcessTypeQueryService judicialProcessTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJudicialProcessTypeMockMvc;

    private JudicialProcessType judicialProcessType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JudicialProcessTypeResource judicialProcessTypeResource = new JudicialProcessTypeResource(judicialProcessTypeService, judicialProcessTypeQueryService);
        this.restJudicialProcessTypeMockMvc = MockMvcBuilders.standaloneSetup(judicialProcessTypeResource)
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
    public static JudicialProcessType createEntity(EntityManager em) {
        JudicialProcessType judicialProcessType = new JudicialProcessType()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return judicialProcessType;
    }

    @Before
    public void initTest() {
        judicialProcessTypeSearchRepository.deleteAll();
        judicialProcessType = createEntity(em);
    }

    @Test
    @Transactional
    public void createJudicialProcessType() throws Exception {
        int databaseSizeBeforeCreate = judicialProcessTypeRepository.findAll().size();

        // Create the JudicialProcessType
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(judicialProcessType);
        restJudicialProcessTypeMockMvc.perform(post("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the JudicialProcessType in the database
        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeCreate + 1);
        JudicialProcessType testJudicialProcessType = judicialProcessTypeList.get(judicialProcessTypeList.size() - 1);
        assertThat(testJudicialProcessType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJudicialProcessType.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the JudicialProcessType in Elasticsearch
        JudicialProcessType judicialProcessTypeEs = judicialProcessTypeSearchRepository.findOne(testJudicialProcessType.getId());
        assertThat(judicialProcessTypeEs).isEqualToIgnoringGivenFields(testJudicialProcessType);
    }

    @Test
    @Transactional
    public void createJudicialProcessTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = judicialProcessTypeRepository.findAll().size();

        // Create the JudicialProcessType with an existing ID
        judicialProcessType.setId(1L);
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(judicialProcessType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJudicialProcessTypeMockMvc.perform(post("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JudicialProcessType in the database
        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = judicialProcessTypeRepository.findAll().size();
        // set the field null
        judicialProcessType.setName(null);

        // Create the JudicialProcessType, which fails.
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(judicialProcessType);

        restJudicialProcessTypeMockMvc.perform(post("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isBadRequest());

        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = judicialProcessTypeRepository.findAll().size();
        // set the field null
        judicialProcessType.setStatus(null);

        // Create the JudicialProcessType, which fails.
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(judicialProcessType);

        restJudicialProcessTypeMockMvc.perform(post("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isBadRequest());

        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypes() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList
        restJudicialProcessTypeMockMvc.perform(get("/api/judicial-process-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(judicialProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getJudicialProcessType() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get the judicialProcessType
        restJudicialProcessTypeMockMvc.perform(get("/api/judicial-process-types/{id}", judicialProcessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(judicialProcessType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where name equals to DEFAULT_NAME
        defaultJudicialProcessTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the judicialProcessTypeList where name equals to UPDATED_NAME
        defaultJudicialProcessTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultJudicialProcessTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the judicialProcessTypeList where name equals to UPDATED_NAME
        defaultJudicialProcessTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where name is not null
        defaultJudicialProcessTypeShouldBeFound("name.specified=true");

        // Get all the judicialProcessTypeList where name is null
        defaultJudicialProcessTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where status equals to DEFAULT_STATUS
        defaultJudicialProcessTypeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the judicialProcessTypeList where status equals to UPDATED_STATUS
        defaultJudicialProcessTypeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultJudicialProcessTypeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the judicialProcessTypeList where status equals to UPDATED_STATUS
        defaultJudicialProcessTypeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllJudicialProcessTypesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);

        // Get all the judicialProcessTypeList where status is not null
        defaultJudicialProcessTypeShouldBeFound("status.specified=true");

        // Get all the judicialProcessTypeList where status is null
        defaultJudicialProcessTypeShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJudicialProcessTypeShouldBeFound(String filter) throws Exception {
        restJudicialProcessTypeMockMvc.perform(get("/api/judicial-process-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(judicialProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJudicialProcessTypeShouldNotBeFound(String filter) throws Exception {
        restJudicialProcessTypeMockMvc.perform(get("/api/judicial-process-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJudicialProcessType() throws Exception {
        // Get the judicialProcessType
        restJudicialProcessTypeMockMvc.perform(get("/api/judicial-process-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJudicialProcessType() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);
        judicialProcessTypeSearchRepository.save(judicialProcessType);
        int databaseSizeBeforeUpdate = judicialProcessTypeRepository.findAll().size();

        // Update the judicialProcessType
        JudicialProcessType updatedJudicialProcessType = judicialProcessTypeRepository.findOne(judicialProcessType.getId());
        // Disconnect from session so that the updates on updatedJudicialProcessType are not directly saved in db
        em.detach(updatedJudicialProcessType);
        updatedJudicialProcessType
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(updatedJudicialProcessType);

        restJudicialProcessTypeMockMvc.perform(put("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isOk());

        // Validate the JudicialProcessType in the database
        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeUpdate);
        JudicialProcessType testJudicialProcessType = judicialProcessTypeList.get(judicialProcessTypeList.size() - 1);
        assertThat(testJudicialProcessType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJudicialProcessType.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the JudicialProcessType in Elasticsearch
        JudicialProcessType judicialProcessTypeEs = judicialProcessTypeSearchRepository.findOne(testJudicialProcessType.getId());
        assertThat(judicialProcessTypeEs).isEqualToIgnoringGivenFields(testJudicialProcessType);
    }

    @Test
    @Transactional
    public void updateNonExistingJudicialProcessType() throws Exception {
        int databaseSizeBeforeUpdate = judicialProcessTypeRepository.findAll().size();

        // Create the JudicialProcessType
        JudicialProcessTypeDTO judicialProcessTypeDTO = judicialProcessTypeMapper.toDto(judicialProcessType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJudicialProcessTypeMockMvc.perform(put("/api/judicial-process-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judicialProcessTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the JudicialProcessType in the database
        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJudicialProcessType() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);
        judicialProcessTypeSearchRepository.save(judicialProcessType);
        int databaseSizeBeforeDelete = judicialProcessTypeRepository.findAll().size();

        // Get the judicialProcessType
        restJudicialProcessTypeMockMvc.perform(delete("/api/judicial-process-types/{id}", judicialProcessType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean judicialProcessTypeExistsInEs = judicialProcessTypeSearchRepository.exists(judicialProcessType.getId());
        assertThat(judicialProcessTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<JudicialProcessType> judicialProcessTypeList = judicialProcessTypeRepository.findAll();
        assertThat(judicialProcessTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJudicialProcessType() throws Exception {
        // Initialize the database
        judicialProcessTypeRepository.saveAndFlush(judicialProcessType);
        judicialProcessTypeSearchRepository.save(judicialProcessType);

        // Search the judicialProcessType
        restJudicialProcessTypeMockMvc.perform(get("/api/_search/judicial-process-types?query=id:" + judicialProcessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(judicialProcessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JudicialProcessType.class);
        JudicialProcessType judicialProcessType1 = new JudicialProcessType();
        judicialProcessType1.setId(1L);
        JudicialProcessType judicialProcessType2 = new JudicialProcessType();
        judicialProcessType2.setId(judicialProcessType1.getId());
        assertThat(judicialProcessType1).isEqualTo(judicialProcessType2);
        judicialProcessType2.setId(2L);
        assertThat(judicialProcessType1).isNotEqualTo(judicialProcessType2);
        judicialProcessType1.setId(null);
        assertThat(judicialProcessType1).isNotEqualTo(judicialProcessType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JudicialProcessTypeDTO.class);
        JudicialProcessTypeDTO judicialProcessTypeDTO1 = new JudicialProcessTypeDTO();
        judicialProcessTypeDTO1.setId(1L);
        JudicialProcessTypeDTO judicialProcessTypeDTO2 = new JudicialProcessTypeDTO();
        assertThat(judicialProcessTypeDTO1).isNotEqualTo(judicialProcessTypeDTO2);
        judicialProcessTypeDTO2.setId(judicialProcessTypeDTO1.getId());
        assertThat(judicialProcessTypeDTO1).isEqualTo(judicialProcessTypeDTO2);
        judicialProcessTypeDTO2.setId(2L);
        assertThat(judicialProcessTypeDTO1).isNotEqualTo(judicialProcessTypeDTO2);
        judicialProcessTypeDTO1.setId(null);
        assertThat(judicialProcessTypeDTO1).isNotEqualTo(judicialProcessTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(judicialProcessTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(judicialProcessTypeMapper.fromId(null)).isNull();
    }
}
