package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.DescaWay;
import mx.gob.scjn.desca.repository.DescaWayRepository;
import mx.gob.scjn.desca.service.DescaWayService;
import mx.gob.scjn.desca.repository.search.DescaWaySearchRepository;
import mx.gob.scjn.desca.service.dto.DescaWayDTO;
import mx.gob.scjn.desca.service.mapper.DescaWayMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.DescaWayCriteria;
import mx.gob.scjn.desca.service.DescaWayQueryService;

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
 * Test class for the DescaWayResource REST controller.
 *
 * @see DescaWayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class DescaWayResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private DescaWayRepository descaWayRepository;

    @Autowired
    private DescaWayMapper descaWayMapper;

    @Autowired
    private DescaWayService descaWayService;

    @Autowired
    private DescaWaySearchRepository descaWaySearchRepository;

    @Autowired
    private DescaWayQueryService descaWayQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescaWayMockMvc;

    private DescaWay descaWay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescaWayResource descaWayResource = new DescaWayResource(descaWayService, descaWayQueryService);
        this.restDescaWayMockMvc = MockMvcBuilders.standaloneSetup(descaWayResource)
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
    public static DescaWay createEntity(EntityManager em) {
        DescaWay descaWay = new DescaWay()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return descaWay;
    }

    @Before
    public void initTest() {
        descaWaySearchRepository.deleteAll();
        descaWay = createEntity(em);
    }

    @Test
    @Transactional
    public void createDescaWay() throws Exception {
        int databaseSizeBeforeCreate = descaWayRepository.findAll().size();

        // Create the DescaWay
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(descaWay);
        restDescaWayMockMvc.perform(post("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isCreated());

        // Validate the DescaWay in the database
        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeCreate + 1);
        DescaWay testDescaWay = descaWayList.get(descaWayList.size() - 1);
        assertThat(testDescaWay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDescaWay.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the DescaWay in Elasticsearch
        DescaWay descaWayEs = descaWaySearchRepository.findOne(testDescaWay.getId());
        assertThat(descaWayEs).isEqualToIgnoringGivenFields(testDescaWay);
    }

    @Test
    @Transactional
    public void createDescaWayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descaWayRepository.findAll().size();

        // Create the DescaWay with an existing ID
        descaWay.setId(1L);
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(descaWay);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescaWayMockMvc.perform(post("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DescaWay in the database
        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = descaWayRepository.findAll().size();
        // set the field null
        descaWay.setName(null);

        // Create the DescaWay, which fails.
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(descaWay);

        restDescaWayMockMvc.perform(post("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isBadRequest());

        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = descaWayRepository.findAll().size();
        // set the field null
        descaWay.setStatus(null);

        // Create the DescaWay, which fails.
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(descaWay);

        restDescaWayMockMvc.perform(post("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isBadRequest());

        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDescaWays() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList
        restDescaWayMockMvc.perform(get("/api/desca-ways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getDescaWay() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get the descaWay
        restDescaWayMockMvc.perform(get("/api/desca-ways/{id}", descaWay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descaWay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDescaWaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where name equals to DEFAULT_NAME
        defaultDescaWayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the descaWayList where name equals to UPDATED_NAME
        defaultDescaWayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescaWaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDescaWayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the descaWayList where name equals to UPDATED_NAME
        defaultDescaWayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDescaWaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where name is not null
        defaultDescaWayShouldBeFound("name.specified=true");

        // Get all the descaWayList where name is null
        defaultDescaWayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDescaWaysByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where status equals to DEFAULT_STATUS
        defaultDescaWayShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the descaWayList where status equals to UPDATED_STATUS
        defaultDescaWayShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDescaWaysByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDescaWayShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the descaWayList where status equals to UPDATED_STATUS
        defaultDescaWayShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllDescaWaysByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);

        // Get all the descaWayList where status is not null
        defaultDescaWayShouldBeFound("status.specified=true");

        // Get all the descaWayList where status is null
        defaultDescaWayShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDescaWayShouldBeFound(String filter) throws Exception {
        restDescaWayMockMvc.perform(get("/api/desca-ways?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDescaWayShouldNotBeFound(String filter) throws Exception {
        restDescaWayMockMvc.perform(get("/api/desca-ways?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDescaWay() throws Exception {
        // Get the descaWay
        restDescaWayMockMvc.perform(get("/api/desca-ways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDescaWay() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);
        descaWaySearchRepository.save(descaWay);
        int databaseSizeBeforeUpdate = descaWayRepository.findAll().size();

        // Update the descaWay
        DescaWay updatedDescaWay = descaWayRepository.findOne(descaWay.getId());
        // Disconnect from session so that the updates on updatedDescaWay are not directly saved in db
        em.detach(updatedDescaWay);
        updatedDescaWay
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(updatedDescaWay);

        restDescaWayMockMvc.perform(put("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isOk());

        // Validate the DescaWay in the database
        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeUpdate);
        DescaWay testDescaWay = descaWayList.get(descaWayList.size() - 1);
        assertThat(testDescaWay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDescaWay.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the DescaWay in Elasticsearch
        DescaWay descaWayEs = descaWaySearchRepository.findOne(testDescaWay.getId());
        assertThat(descaWayEs).isEqualToIgnoringGivenFields(testDescaWay);
    }

    @Test
    @Transactional
    public void updateNonExistingDescaWay() throws Exception {
        int databaseSizeBeforeUpdate = descaWayRepository.findAll().size();

        // Create the DescaWay
        DescaWayDTO descaWayDTO = descaWayMapper.toDto(descaWay);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescaWayMockMvc.perform(put("/api/desca-ways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descaWayDTO)))
            .andExpect(status().isCreated());

        // Validate the DescaWay in the database
        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDescaWay() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);
        descaWaySearchRepository.save(descaWay);
        int databaseSizeBeforeDelete = descaWayRepository.findAll().size();

        // Get the descaWay
        restDescaWayMockMvc.perform(delete("/api/desca-ways/{id}", descaWay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean descaWayExistsInEs = descaWaySearchRepository.exists(descaWay.getId());
        assertThat(descaWayExistsInEs).isFalse();

        // Validate the database is empty
        List<DescaWay> descaWayList = descaWayRepository.findAll();
        assertThat(descaWayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDescaWay() throws Exception {
        // Initialize the database
        descaWayRepository.saveAndFlush(descaWay);
        descaWaySearchRepository.save(descaWay);

        // Search the descaWay
        restDescaWayMockMvc.perform(get("/api/_search/desca-ways?query=id:" + descaWay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descaWay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescaWay.class);
        DescaWay descaWay1 = new DescaWay();
        descaWay1.setId(1L);
        DescaWay descaWay2 = new DescaWay();
        descaWay2.setId(descaWay1.getId());
        assertThat(descaWay1).isEqualTo(descaWay2);
        descaWay2.setId(2L);
        assertThat(descaWay1).isNotEqualTo(descaWay2);
        descaWay1.setId(null);
        assertThat(descaWay1).isNotEqualTo(descaWay2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescaWayDTO.class);
        DescaWayDTO descaWayDTO1 = new DescaWayDTO();
        descaWayDTO1.setId(1L);
        DescaWayDTO descaWayDTO2 = new DescaWayDTO();
        assertThat(descaWayDTO1).isNotEqualTo(descaWayDTO2);
        descaWayDTO2.setId(descaWayDTO1.getId());
        assertThat(descaWayDTO1).isEqualTo(descaWayDTO2);
        descaWayDTO2.setId(2L);
        assertThat(descaWayDTO1).isNotEqualTo(descaWayDTO2);
        descaWayDTO1.setId(null);
        assertThat(descaWayDTO1).isNotEqualTo(descaWayDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(descaWayMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(descaWayMapper.fromId(null)).isNull();
    }
}
