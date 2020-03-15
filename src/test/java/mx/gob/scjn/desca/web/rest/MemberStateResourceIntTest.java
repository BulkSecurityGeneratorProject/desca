package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.MemberState;
import mx.gob.scjn.desca.repository.MemberStateRepository;
import mx.gob.scjn.desca.service.MemberStateService;
import mx.gob.scjn.desca.repository.search.MemberStateSearchRepository;
import mx.gob.scjn.desca.service.dto.MemberStateDTO;
import mx.gob.scjn.desca.service.mapper.MemberStateMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.MemberStateCriteria;
import mx.gob.scjn.desca.service.MemberStateQueryService;

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
 * Test class for the MemberStateResource REST controller.
 *
 * @see MemberStateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class MemberStateResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private MemberStateRepository memberStateRepository;

    @Autowired
    private MemberStateMapper memberStateMapper;

    @Autowired
    private MemberStateService memberStateService;

    @Autowired
    private MemberStateSearchRepository memberStateSearchRepository;

    @Autowired
    private MemberStateQueryService memberStateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMemberStateMockMvc;

    private MemberState memberState;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberStateResource memberStateResource = new MemberStateResource(memberStateService, memberStateQueryService);
        this.restMemberStateMockMvc = MockMvcBuilders.standaloneSetup(memberStateResource)
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
    public static MemberState createEntity(EntityManager em) {
        MemberState memberState = new MemberState()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return memberState;
    }

    @Before
    public void initTest() {
        memberStateSearchRepository.deleteAll();
        memberState = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberState() throws Exception {
        int databaseSizeBeforeCreate = memberStateRepository.findAll().size();

        // Create the MemberState
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(memberState);
        restMemberStateMockMvc.perform(post("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isCreated());

        // Validate the MemberState in the database
        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeCreate + 1);
        MemberState testMemberState = memberStateList.get(memberStateList.size() - 1);
        assertThat(testMemberState.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMemberState.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MemberState in Elasticsearch
        MemberState memberStateEs = memberStateSearchRepository.findOne(testMemberState.getId());
        assertThat(memberStateEs).isEqualToIgnoringGivenFields(testMemberState);
    }

    @Test
    @Transactional
    public void createMemberStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberStateRepository.findAll().size();

        // Create the MemberState with an existing ID
        memberState.setId(1L);
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(memberState);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberStateMockMvc.perform(post("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MemberState in the database
        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberStateRepository.findAll().size();
        // set the field null
        memberState.setName(null);

        // Create the MemberState, which fails.
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(memberState);

        restMemberStateMockMvc.perform(post("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isBadRequest());

        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberStateRepository.findAll().size();
        // set the field null
        memberState.setStatus(null);

        // Create the MemberState, which fails.
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(memberState);

        restMemberStateMockMvc.perform(post("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isBadRequest());

        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberStates() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList
        restMemberStateMockMvc.perform(get("/api/member-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberState.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMemberState() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get the memberState
        restMemberStateMockMvc.perform(get("/api/member-states/{id}", memberState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(memberState.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMemberStatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where name equals to DEFAULT_NAME
        defaultMemberStateShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the memberStateList where name equals to UPDATED_NAME
        defaultMemberStateShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberStatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMemberStateShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the memberStateList where name equals to UPDATED_NAME
        defaultMemberStateShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMemberStatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where name is not null
        defaultMemberStateShouldBeFound("name.specified=true");

        // Get all the memberStateList where name is null
        defaultMemberStateShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberStatesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where status equals to DEFAULT_STATUS
        defaultMemberStateShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the memberStateList where status equals to UPDATED_STATUS
        defaultMemberStateShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMemberStatesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMemberStateShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the memberStateList where status equals to UPDATED_STATUS
        defaultMemberStateShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMemberStatesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);

        // Get all the memberStateList where status is not null
        defaultMemberStateShouldBeFound("status.specified=true");

        // Get all the memberStateList where status is null
        defaultMemberStateShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMemberStateShouldBeFound(String filter) throws Exception {
        restMemberStateMockMvc.perform(get("/api/member-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberState.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMemberStateShouldNotBeFound(String filter) throws Exception {
        restMemberStateMockMvc.perform(get("/api/member-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMemberState() throws Exception {
        // Get the memberState
        restMemberStateMockMvc.perform(get("/api/member-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberState() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);
        memberStateSearchRepository.save(memberState);
        int databaseSizeBeforeUpdate = memberStateRepository.findAll().size();

        // Update the memberState
        MemberState updatedMemberState = memberStateRepository.findOne(memberState.getId());
        // Disconnect from session so that the updates on updatedMemberState are not directly saved in db
        em.detach(updatedMemberState);
        updatedMemberState
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(updatedMemberState);

        restMemberStateMockMvc.perform(put("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isOk());

        // Validate the MemberState in the database
        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeUpdate);
        MemberState testMemberState = memberStateList.get(memberStateList.size() - 1);
        assertThat(testMemberState.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMemberState.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MemberState in Elasticsearch
        MemberState memberStateEs = memberStateSearchRepository.findOne(testMemberState.getId());
        assertThat(memberStateEs).isEqualToIgnoringGivenFields(testMemberState);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberState() throws Exception {
        int databaseSizeBeforeUpdate = memberStateRepository.findAll().size();

        // Create the MemberState
        MemberStateDTO memberStateDTO = memberStateMapper.toDto(memberState);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMemberStateMockMvc.perform(put("/api/member-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberStateDTO)))
            .andExpect(status().isCreated());

        // Validate the MemberState in the database
        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMemberState() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);
        memberStateSearchRepository.save(memberState);
        int databaseSizeBeforeDelete = memberStateRepository.findAll().size();

        // Get the memberState
        restMemberStateMockMvc.perform(delete("/api/member-states/{id}", memberState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean memberStateExistsInEs = memberStateSearchRepository.exists(memberState.getId());
        assertThat(memberStateExistsInEs).isFalse();

        // Validate the database is empty
        List<MemberState> memberStateList = memberStateRepository.findAll();
        assertThat(memberStateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMemberState() throws Exception {
        // Initialize the database
        memberStateRepository.saveAndFlush(memberState);
        memberStateSearchRepository.save(memberState);

        // Search the memberState
        restMemberStateMockMvc.perform(get("/api/_search/member-states?query=id:" + memberState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberState.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberState.class);
        MemberState memberState1 = new MemberState();
        memberState1.setId(1L);
        MemberState memberState2 = new MemberState();
        memberState2.setId(memberState1.getId());
        assertThat(memberState1).isEqualTo(memberState2);
        memberState2.setId(2L);
        assertThat(memberState1).isNotEqualTo(memberState2);
        memberState1.setId(null);
        assertThat(memberState1).isNotEqualTo(memberState2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberStateDTO.class);
        MemberStateDTO memberStateDTO1 = new MemberStateDTO();
        memberStateDTO1.setId(1L);
        MemberStateDTO memberStateDTO2 = new MemberStateDTO();
        assertThat(memberStateDTO1).isNotEqualTo(memberStateDTO2);
        memberStateDTO2.setId(memberStateDTO1.getId());
        assertThat(memberStateDTO1).isEqualTo(memberStateDTO2);
        memberStateDTO2.setId(2L);
        assertThat(memberStateDTO1).isNotEqualTo(memberStateDTO2);
        memberStateDTO1.setId(null);
        assertThat(memberStateDTO1).isNotEqualTo(memberStateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(memberStateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(memberStateMapper.fromId(null)).isNull();
    }
}
