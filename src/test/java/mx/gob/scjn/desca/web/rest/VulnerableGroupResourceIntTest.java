package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.VulnerableGroup;
import mx.gob.scjn.desca.repository.VulnerableGroupRepository;
import mx.gob.scjn.desca.service.VulnerableGroupService;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.VulnerableGroupCriteria;
import mx.gob.scjn.desca.service.VulnerableGroupQueryService;

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
 * Test class for the VulnerableGroupResource REST controller.
 *
 * @see VulnerableGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class VulnerableGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private VulnerableGroupRepository vulnerableGroupRepository;

    @Autowired
    private VulnerableGroupService vulnerableGroupService;

    @Autowired
    private VulnerableGroupQueryService vulnerableGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVulnerableGroupMockMvc;

    private VulnerableGroup vulnerableGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VulnerableGroupResource vulnerableGroupResource = new VulnerableGroupResource(vulnerableGroupService, vulnerableGroupQueryService);
        this.restVulnerableGroupMockMvc = MockMvcBuilders.standaloneSetup(vulnerableGroupResource)
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
    public static VulnerableGroup createEntity(EntityManager em) {
        VulnerableGroup vulnerableGroup = new VulnerableGroup()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return vulnerableGroup;
    }

    @Before
    public void initTest() {
        vulnerableGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createVulnerableGroup() throws Exception {
        int databaseSizeBeforeCreate = vulnerableGroupRepository.findAll().size();

        // Create the VulnerableGroup
        restVulnerableGroupMockMvc.perform(post("/api/vulnerable-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vulnerableGroup)))
            .andExpect(status().isCreated());

        // Validate the VulnerableGroup in the database
        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeCreate + 1);
        VulnerableGroup testVulnerableGroup = vulnerableGroupList.get(vulnerableGroupList.size() - 1);
        assertThat(testVulnerableGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVulnerableGroup.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createVulnerableGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vulnerableGroupRepository.findAll().size();

        // Create the VulnerableGroup with an existing ID
        vulnerableGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVulnerableGroupMockMvc.perform(post("/api/vulnerable-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vulnerableGroup)))
            .andExpect(status().isBadRequest());

        // Validate the VulnerableGroup in the database
        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vulnerableGroupRepository.findAll().size();
        // set the field null
        vulnerableGroup.setName(null);

        // Create the VulnerableGroup, which fails.

        restVulnerableGroupMockMvc.perform(post("/api/vulnerable-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vulnerableGroup)))
            .andExpect(status().isBadRequest());

        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVulnerableGroups() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList
        restVulnerableGroupMockMvc.perform(get("/api/vulnerable-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vulnerableGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getVulnerableGroup() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get the vulnerableGroup
        restVulnerableGroupMockMvc.perform(get("/api/vulnerable-groups/{id}", vulnerableGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vulnerableGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where name equals to DEFAULT_NAME
        defaultVulnerableGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vulnerableGroupList where name equals to UPDATED_NAME
        defaultVulnerableGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVulnerableGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vulnerableGroupList where name equals to UPDATED_NAME
        defaultVulnerableGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where name is not null
        defaultVulnerableGroupShouldBeFound("name.specified=true");

        // Get all the vulnerableGroupList where name is null
        defaultVulnerableGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where status equals to DEFAULT_STATUS
        defaultVulnerableGroupShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the vulnerableGroupList where status equals to UPDATED_STATUS
        defaultVulnerableGroupShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultVulnerableGroupShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the vulnerableGroupList where status equals to UPDATED_STATUS
        defaultVulnerableGroupShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllVulnerableGroupsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        vulnerableGroupRepository.saveAndFlush(vulnerableGroup);

        // Get all the vulnerableGroupList where status is not null
        defaultVulnerableGroupShouldBeFound("status.specified=true");

        // Get all the vulnerableGroupList where status is null
        defaultVulnerableGroupShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVulnerableGroupShouldBeFound(String filter) throws Exception {
        restVulnerableGroupMockMvc.perform(get("/api/vulnerable-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vulnerableGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVulnerableGroupShouldNotBeFound(String filter) throws Exception {
        restVulnerableGroupMockMvc.perform(get("/api/vulnerable-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVulnerableGroup() throws Exception {
        // Get the vulnerableGroup
        restVulnerableGroupMockMvc.perform(get("/api/vulnerable-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVulnerableGroup() throws Exception {
        // Initialize the database
        vulnerableGroupService.save(vulnerableGroup);

        int databaseSizeBeforeUpdate = vulnerableGroupRepository.findAll().size();

        // Update the vulnerableGroup
        VulnerableGroup updatedVulnerableGroup = vulnerableGroupRepository.findOne(vulnerableGroup.getId());
        // Disconnect from session so that the updates on updatedVulnerableGroup are not directly saved in db
        em.detach(updatedVulnerableGroup);
        updatedVulnerableGroup
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);

        restVulnerableGroupMockMvc.perform(put("/api/vulnerable-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVulnerableGroup)))
            .andExpect(status().isOk());

        // Validate the VulnerableGroup in the database
        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeUpdate);
        VulnerableGroup testVulnerableGroup = vulnerableGroupList.get(vulnerableGroupList.size() - 1);
        assertThat(testVulnerableGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVulnerableGroup.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingVulnerableGroup() throws Exception {
        int databaseSizeBeforeUpdate = vulnerableGroupRepository.findAll().size();

        // Create the VulnerableGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVulnerableGroupMockMvc.perform(put("/api/vulnerable-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vulnerableGroup)))
            .andExpect(status().isCreated());

        // Validate the VulnerableGroup in the database
        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVulnerableGroup() throws Exception {
        // Initialize the database
        vulnerableGroupService.save(vulnerableGroup);

        int databaseSizeBeforeDelete = vulnerableGroupRepository.findAll().size();

        // Get the vulnerableGroup
        restVulnerableGroupMockMvc.perform(delete("/api/vulnerable-groups/{id}", vulnerableGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VulnerableGroup> vulnerableGroupList = vulnerableGroupRepository.findAll();
        assertThat(vulnerableGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VulnerableGroup.class);
        VulnerableGroup vulnerableGroup1 = new VulnerableGroup();
        vulnerableGroup1.setId(1L);
        VulnerableGroup vulnerableGroup2 = new VulnerableGroup();
        vulnerableGroup2.setId(vulnerableGroup1.getId());
        assertThat(vulnerableGroup1).isEqualTo(vulnerableGroup2);
        vulnerableGroup2.setId(2L);
        assertThat(vulnerableGroup1).isNotEqualTo(vulnerableGroup2);
        vulnerableGroup1.setId(null);
        assertThat(vulnerableGroup1).isNotEqualTo(vulnerableGroup2);
    }
}
