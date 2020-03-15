package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.MainDatabase;
import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.domain.JudicialProcessType;
import mx.gob.scjn.desca.repository.MainDatabaseRepository;
import mx.gob.scjn.desca.service.MainDatabaseService;
import mx.gob.scjn.desca.repository.search.MainDatabaseSearchRepository;
import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;
import mx.gob.scjn.desca.service.mapper.MainDatabaseMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.MainDatabaseCriteria;
import mx.gob.scjn.desca.service.MainDatabaseQueryService;

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
 * Test class for the MainDatabaseResource REST controller.
 *
 * @see MainDatabaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class MainDatabaseResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_INTITUTION = "BBBBBBBBBB";

    @Autowired
    private MainDatabaseRepository mainDatabaseRepository;

    @Autowired
    private MainDatabaseMapper mainDatabaseMapper;

    @Autowired
    private MainDatabaseService mainDatabaseService;

    @Autowired
    private MainDatabaseSearchRepository mainDatabaseSearchRepository;

    @Autowired
    private MainDatabaseQueryService mainDatabaseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMainDatabaseMockMvc;

    private MainDatabase mainDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MainDatabaseResource mainDatabaseResource = new MainDatabaseResource(mainDatabaseService, mainDatabaseQueryService);
        this.restMainDatabaseMockMvc = MockMvcBuilders.standaloneSetup(mainDatabaseResource)
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
    public static MainDatabase createEntity(EntityManager em) {
        MainDatabase mainDatabase = new MainDatabase()
            .number(DEFAULT_NUMBER)
            .intitution(DEFAULT_INTITUTION);
        return mainDatabase;
    }

    @Before
    public void initTest() {
        mainDatabaseSearchRepository.deleteAll();
        mainDatabase = createEntity(em);
    }

    @Test
    @Transactional
    public void createMainDatabase() throws Exception {
        int databaseSizeBeforeCreate = mainDatabaseRepository.findAll().size();

        // Create the MainDatabase
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseMapper.toDto(mainDatabase);
        restMainDatabaseMockMvc.perform(post("/api/main-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mainDatabaseDTO)))
            .andExpect(status().isCreated());

        // Validate the MainDatabase in the database
        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeCreate + 1);
        MainDatabase testMainDatabase = mainDatabaseList.get(mainDatabaseList.size() - 1);
        assertThat(testMainDatabase.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testMainDatabase.getIntitution()).isEqualTo(DEFAULT_INTITUTION);

        // Validate the MainDatabase in Elasticsearch
        MainDatabase mainDatabaseEs = mainDatabaseSearchRepository.findOne(testMainDatabase.getId());
        assertThat(mainDatabaseEs).isEqualToIgnoringGivenFields(testMainDatabase);
    }

    @Test
    @Transactional
    public void createMainDatabaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mainDatabaseRepository.findAll().size();

        // Create the MainDatabase with an existing ID
        mainDatabase.setId(1L);
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseMapper.toDto(mainDatabase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMainDatabaseMockMvc.perform(post("/api/main-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mainDatabaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MainDatabase in the database
        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = mainDatabaseRepository.findAll().size();
        // set the field null
        mainDatabase.setNumber(null);

        // Create the MainDatabase, which fails.
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseMapper.toDto(mainDatabase);

        restMainDatabaseMockMvc.perform(post("/api/main-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mainDatabaseDTO)))
            .andExpect(status().isBadRequest());

        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMainDatabases() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList
        restMainDatabaseMockMvc.perform(get("/api/main-databases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainDatabase.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].intitution").value(hasItem(DEFAULT_INTITUTION.toString())));
    }

    @Test
    @Transactional
    public void getMainDatabase() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get the mainDatabase
        restMainDatabaseMockMvc.perform(get("/api/main-databases/{id}", mainDatabase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mainDatabase.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.intitution").value(DEFAULT_INTITUTION.toString()));
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where number equals to DEFAULT_NUMBER
        defaultMainDatabaseShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the mainDatabaseList where number equals to UPDATED_NUMBER
        defaultMainDatabaseShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultMainDatabaseShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the mainDatabaseList where number equals to UPDATED_NUMBER
        defaultMainDatabaseShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where number is not null
        defaultMainDatabaseShouldBeFound("number.specified=true");

        // Get all the mainDatabaseList where number is null
        defaultMainDatabaseShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByIntitutionIsEqualToSomething() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where intitution equals to DEFAULT_INTITUTION
        defaultMainDatabaseShouldBeFound("intitution.equals=" + DEFAULT_INTITUTION);

        // Get all the mainDatabaseList where intitution equals to UPDATED_INTITUTION
        defaultMainDatabaseShouldNotBeFound("intitution.equals=" + UPDATED_INTITUTION);
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByIntitutionIsInShouldWork() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where intitution in DEFAULT_INTITUTION or UPDATED_INTITUTION
        defaultMainDatabaseShouldBeFound("intitution.in=" + DEFAULT_INTITUTION + "," + UPDATED_INTITUTION);

        // Get all the mainDatabaseList where intitution equals to UPDATED_INTITUTION
        defaultMainDatabaseShouldNotBeFound("intitution.in=" + UPDATED_INTITUTION);
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByIntitutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);

        // Get all the mainDatabaseList where intitution is not null
        defaultMainDatabaseShouldBeFound("intitution.specified=true");

        // Get all the mainDatabaseList where intitution is null
        defaultMainDatabaseShouldNotBeFound("intitution.specified=false");
    }

    @Test
    @Transactional
    public void getAllMainDatabasesByMemberStateIsEqualToSomething() throws Exception {
        // Initialize the database
        MemberState memberState = MemberStateResourceIntTest.createEntity(em);
        em.persist(memberState);
        em.flush();
        mainDatabase.setMemberState(memberState);
        mainDatabaseRepository.saveAndFlush(mainDatabase);
        Long memberStateId = memberState.getId();

        // Get all the mainDatabaseList where memberState equals to memberStateId
        defaultMainDatabaseShouldBeFound("memberStateId.equals=" + memberStateId);

        // Get all the mainDatabaseList where memberState equals to memberStateId + 1
        defaultMainDatabaseShouldNotBeFound("memberStateId.equals=" + (memberStateId + 1));
    }


    @Test
    @Transactional
    public void getAllMainDatabasesByJudicialProcessTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        JudicialProcessType judicialProcessType = JudicialProcessTypeResourceIntTest.createEntity(em);
        em.persist(judicialProcessType);
        em.flush();
        mainDatabase.setJudicialProcessType(judicialProcessType);
        mainDatabaseRepository.saveAndFlush(mainDatabase);
        Long judicialProcessTypeId = judicialProcessType.getId();

        // Get all the mainDatabaseList where judicialProcessType equals to judicialProcessTypeId
        defaultMainDatabaseShouldBeFound("judicialProcessTypeId.equals=" + judicialProcessTypeId);

        // Get all the mainDatabaseList where judicialProcessType equals to judicialProcessTypeId + 1
        defaultMainDatabaseShouldNotBeFound("judicialProcessTypeId.equals=" + (judicialProcessTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMainDatabaseShouldBeFound(String filter) throws Exception {
        restMainDatabaseMockMvc.perform(get("/api/main-databases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainDatabase.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].intitution").value(hasItem(DEFAULT_INTITUTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMainDatabaseShouldNotBeFound(String filter) throws Exception {
        restMainDatabaseMockMvc.perform(get("/api/main-databases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMainDatabase() throws Exception {
        // Get the mainDatabase
        restMainDatabaseMockMvc.perform(get("/api/main-databases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMainDatabase() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);
        mainDatabaseSearchRepository.save(mainDatabase);
        int databaseSizeBeforeUpdate = mainDatabaseRepository.findAll().size();

        // Update the mainDatabase
        MainDatabase updatedMainDatabase = mainDatabaseRepository.findOne(mainDatabase.getId());
        // Disconnect from session so that the updates on updatedMainDatabase are not directly saved in db
        em.detach(updatedMainDatabase);
        updatedMainDatabase
            .number(UPDATED_NUMBER)
            .intitution(UPDATED_INTITUTION);
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseMapper.toDto(updatedMainDatabase);

        restMainDatabaseMockMvc.perform(put("/api/main-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mainDatabaseDTO)))
            .andExpect(status().isOk());

        // Validate the MainDatabase in the database
        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeUpdate);
        MainDatabase testMainDatabase = mainDatabaseList.get(mainDatabaseList.size() - 1);
        assertThat(testMainDatabase.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testMainDatabase.getIntitution()).isEqualTo(UPDATED_INTITUTION);

        // Validate the MainDatabase in Elasticsearch
        MainDatabase mainDatabaseEs = mainDatabaseSearchRepository.findOne(testMainDatabase.getId());
        assertThat(mainDatabaseEs).isEqualToIgnoringGivenFields(testMainDatabase);
    }

    @Test
    @Transactional
    public void updateNonExistingMainDatabase() throws Exception {
        int databaseSizeBeforeUpdate = mainDatabaseRepository.findAll().size();

        // Create the MainDatabase
        MainDatabaseDTO mainDatabaseDTO = mainDatabaseMapper.toDto(mainDatabase);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMainDatabaseMockMvc.perform(put("/api/main-databases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mainDatabaseDTO)))
            .andExpect(status().isCreated());

        // Validate the MainDatabase in the database
        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMainDatabase() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);
        mainDatabaseSearchRepository.save(mainDatabase);
        int databaseSizeBeforeDelete = mainDatabaseRepository.findAll().size();

        // Get the mainDatabase
        restMainDatabaseMockMvc.perform(delete("/api/main-databases/{id}", mainDatabase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mainDatabaseExistsInEs = mainDatabaseSearchRepository.exists(mainDatabase.getId());
        assertThat(mainDatabaseExistsInEs).isFalse();

        // Validate the database is empty
        List<MainDatabase> mainDatabaseList = mainDatabaseRepository.findAll();
        assertThat(mainDatabaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMainDatabase() throws Exception {
        // Initialize the database
        mainDatabaseRepository.saveAndFlush(mainDatabase);
        mainDatabaseSearchRepository.save(mainDatabase);

        // Search the mainDatabase
        restMainDatabaseMockMvc.perform(get("/api/_search/main-databases?query=id:" + mainDatabase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainDatabase.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].intitution").value(hasItem(DEFAULT_INTITUTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainDatabase.class);
        MainDatabase mainDatabase1 = new MainDatabase();
        mainDatabase1.setId(1L);
        MainDatabase mainDatabase2 = new MainDatabase();
        mainDatabase2.setId(mainDatabase1.getId());
        assertThat(mainDatabase1).isEqualTo(mainDatabase2);
        mainDatabase2.setId(2L);
        assertThat(mainDatabase1).isNotEqualTo(mainDatabase2);
        mainDatabase1.setId(null);
        assertThat(mainDatabase1).isNotEqualTo(mainDatabase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainDatabaseDTO.class);
        MainDatabaseDTO mainDatabaseDTO1 = new MainDatabaseDTO();
        mainDatabaseDTO1.setId(1L);
        MainDatabaseDTO mainDatabaseDTO2 = new MainDatabaseDTO();
        assertThat(mainDatabaseDTO1).isNotEqualTo(mainDatabaseDTO2);
        mainDatabaseDTO2.setId(mainDatabaseDTO1.getId());
        assertThat(mainDatabaseDTO1).isEqualTo(mainDatabaseDTO2);
        mainDatabaseDTO2.setId(2L);
        assertThat(mainDatabaseDTO1).isNotEqualTo(mainDatabaseDTO2);
        mainDatabaseDTO1.setId(null);
        assertThat(mainDatabaseDTO1).isNotEqualTo(mainDatabaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mainDatabaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mainDatabaseMapper.fromId(null)).isNull();
    }
}
