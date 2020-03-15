package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.Repair;
import mx.gob.scjn.desca.domain.RepairType;
import mx.gob.scjn.desca.repository.RepairRepository;
import mx.gob.scjn.desca.service.RepairService;
import mx.gob.scjn.desca.repository.search.RepairSearchRepository;
import mx.gob.scjn.desca.service.dto.RepairDTO;
import mx.gob.scjn.desca.service.mapper.RepairMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.RepairCriteria;
import mx.gob.scjn.desca.service.RepairQueryService;

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
 * Test class for the RepairResource REST controller.
 *
 * @see RepairResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class RepairResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private RepairService repairService;

    @Autowired
    private RepairSearchRepository repairSearchRepository;

    @Autowired
    private RepairQueryService repairQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRepairMockMvc;

    private Repair repair;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RepairResource repairResource = new RepairResource(repairService, repairQueryService);
        this.restRepairMockMvc = MockMvcBuilders.standaloneSetup(repairResource)
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
    public static Repair createEntity(EntityManager em) {
        Repair repair = new Repair()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return repair;
    }

    @Before
    public void initTest() {
        repairSearchRepository.deleteAll();
        repair = createEntity(em);
    }

    @Test
    @Transactional
    public void createRepair() throws Exception {
        int databaseSizeBeforeCreate = repairRepository.findAll().size();

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);
        restRepairMockMvc.perform(post("/api/repairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
            .andExpect(status().isCreated());

        // Validate the Repair in the database
        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeCreate + 1);
        Repair testRepair = repairList.get(repairList.size() - 1);
        assertThat(testRepair.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepair.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the Repair in Elasticsearch
        Repair repairEs = repairSearchRepository.findOne(testRepair.getId());
        assertThat(repairEs).isEqualToIgnoringGivenFields(testRepair);
    }

    @Test
    @Transactional
    public void createRepairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repairRepository.findAll().size();

        // Create the Repair with an existing ID
        repair.setId(1L);
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairMockMvc.perform(post("/api/repairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairRepository.findAll().size();
        // set the field null
        repair.setName(null);

        // Create the Repair, which fails.
        RepairDTO repairDTO = repairMapper.toDto(repair);

        restRepairMockMvc.perform(post("/api/repairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
            .andExpect(status().isBadRequest());

        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepairs() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList
        restRepairMockMvc.perform(get("/api/repairs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get the repair
        restRepairMockMvc.perform(get("/api/repairs/{id}", repair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(repair.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRepairsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where name equals to DEFAULT_NAME
        defaultRepairShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the repairList where name equals to UPDATED_NAME
        defaultRepairShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRepairShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the repairList where name equals to UPDATED_NAME
        defaultRepairShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where name is not null
        defaultRepairShouldBeFound("name.specified=true");

        // Get all the repairList where name is null
        defaultRepairShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRepairsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where enabled equals to DEFAULT_ENABLED
        defaultRepairShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the repairList where enabled equals to UPDATED_ENABLED
        defaultRepairShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRepairsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultRepairShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the repairList where enabled equals to UPDATED_ENABLED
        defaultRepairShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllRepairsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairList where enabled is not null
        defaultRepairShouldBeFound("enabled.specified=true");

        // Get all the repairList where enabled is null
        defaultRepairShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllRepairsByRepairTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        RepairType repairType = RepairTypeResourceIntTest.createEntity(em);
        em.persist(repairType);
        em.flush();
        repair.setRepairType(repairType);
        repairRepository.saveAndFlush(repair);
        Long repairTypeId = repairType.getId();

        // Get all the repairList where repairType equals to repairTypeId
        defaultRepairShouldBeFound("repairTypeId.equals=" + repairTypeId);

        // Get all the repairList where repairType equals to repairTypeId + 1
        defaultRepairShouldNotBeFound("repairTypeId.equals=" + (repairTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRepairShouldBeFound(String filter) throws Exception {
        restRepairMockMvc.perform(get("/api/repairs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRepairShouldNotBeFound(String filter) throws Exception {
        restRepairMockMvc.perform(get("/api/repairs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRepair() throws Exception {
        // Get the repair
        restRepairMockMvc.perform(get("/api/repairs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);
        repairSearchRepository.save(repair);
        int databaseSizeBeforeUpdate = repairRepository.findAll().size();

        // Update the repair
        Repair updatedRepair = repairRepository.findOne(repair.getId());
        // Disconnect from session so that the updates on updatedRepair are not directly saved in db
        em.detach(updatedRepair);
        updatedRepair
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        RepairDTO repairDTO = repairMapper.toDto(updatedRepair);

        restRepairMockMvc.perform(put("/api/repairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
            .andExpect(status().isOk());

        // Validate the Repair in the database
        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeUpdate);
        Repair testRepair = repairList.get(repairList.size() - 1);
        assertThat(testRepair.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepair.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the Repair in Elasticsearch
        Repair repairEs = repairSearchRepository.findOne(testRepair.getId());
        assertThat(repairEs).isEqualToIgnoringGivenFields(testRepair);
    }

    @Test
    @Transactional
    public void updateNonExistingRepair() throws Exception {
        int databaseSizeBeforeUpdate = repairRepository.findAll().size();

        // Create the Repair
        RepairDTO repairDTO = repairMapper.toDto(repair);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRepairMockMvc.perform(put("/api/repairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
            .andExpect(status().isCreated());

        // Validate the Repair in the database
        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);
        repairSearchRepository.save(repair);
        int databaseSizeBeforeDelete = repairRepository.findAll().size();

        // Get the repair
        restRepairMockMvc.perform(delete("/api/repairs/{id}", repair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean repairExistsInEs = repairSearchRepository.exists(repair.getId());
        assertThat(repairExistsInEs).isFalse();

        // Validate the database is empty
        List<Repair> repairList = repairRepository.findAll();
        assertThat(repairList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);
        repairSearchRepository.save(repair);

        // Search the repair
        restRepairMockMvc.perform(get("/api/_search/repairs?query=id:" + repair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repair.class);
        Repair repair1 = new Repair();
        repair1.setId(1L);
        Repair repair2 = new Repair();
        repair2.setId(repair1.getId());
        assertThat(repair1).isEqualTo(repair2);
        repair2.setId(2L);
        assertThat(repair1).isNotEqualTo(repair2);
        repair1.setId(null);
        assertThat(repair1).isNotEqualTo(repair2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairDTO.class);
        RepairDTO repairDTO1 = new RepairDTO();
        repairDTO1.setId(1L);
        RepairDTO repairDTO2 = new RepairDTO();
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
        repairDTO2.setId(repairDTO1.getId());
        assertThat(repairDTO1).isEqualTo(repairDTO2);
        repairDTO2.setId(2L);
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
        repairDTO1.setId(null);
        assertThat(repairDTO1).isNotEqualTo(repairDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(repairMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(repairMapper.fromId(null)).isNull();
    }
}
