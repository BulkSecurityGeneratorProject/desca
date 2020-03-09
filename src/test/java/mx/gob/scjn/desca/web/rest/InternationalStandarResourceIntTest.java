package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.InternationalStandar;
import mx.gob.scjn.desca.repository.InternationalStandarRepository;
import mx.gob.scjn.desca.service.InternationalStandarService;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.InternationalStandarCriteria;
import mx.gob.scjn.desca.service.InternationalStandarQueryService;

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
 * Test class for the InternationalStandarResource REST controller.
 *
 * @see InternationalStandarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class InternationalStandarResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private InternationalStandarRepository internationalStandarRepository;

    @Autowired
    private InternationalStandarService internationalStandarService;

    @Autowired
    private InternationalStandarQueryService internationalStandarQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternationalStandarMockMvc;

    private InternationalStandar internationalStandar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InternationalStandarResource internationalStandarResource = new InternationalStandarResource(internationalStandarService, internationalStandarQueryService);
        this.restInternationalStandarMockMvc = MockMvcBuilders.standaloneSetup(internationalStandarResource)
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
    public static InternationalStandar createEntity(EntityManager em) {
        InternationalStandar internationalStandar = new InternationalStandar()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return internationalStandar;
    }

    @Before
    public void initTest() {
        internationalStandar = createEntity(em);
    }

    @Test
    @Transactional
    public void createInternationalStandar() throws Exception {
        int databaseSizeBeforeCreate = internationalStandarRepository.findAll().size();

        // Create the InternationalStandar
        restInternationalStandarMockMvc.perform(post("/api/international-standars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandar)))
            .andExpect(status().isCreated());

        // Validate the InternationalStandar in the database
        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeCreate + 1);
        InternationalStandar testInternationalStandar = internationalStandarList.get(internationalStandarList.size() - 1);
        assertThat(testInternationalStandar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInternationalStandar.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createInternationalStandarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internationalStandarRepository.findAll().size();

        // Create the InternationalStandar with an existing ID
        internationalStandar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternationalStandarMockMvc.perform(post("/api/international-standars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandar)))
            .andExpect(status().isBadRequest());

        // Validate the InternationalStandar in the database
        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = internationalStandarRepository.findAll().size();
        // set the field null
        internationalStandar.setName(null);

        // Create the InternationalStandar, which fails.

        restInternationalStandarMockMvc.perform(post("/api/international-standars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandar)))
            .andExpect(status().isBadRequest());

        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInternationalStandars() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList
        restInternationalStandarMockMvc.perform(get("/api/international-standars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalStandar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getInternationalStandar() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get the internationalStandar
        restInternationalStandarMockMvc.perform(get("/api/international-standars/{id}", internationalStandar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(internationalStandar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where name equals to DEFAULT_NAME
        defaultInternationalStandarShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the internationalStandarList where name equals to UPDATED_NAME
        defaultInternationalStandarShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInternationalStandarShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the internationalStandarList where name equals to UPDATED_NAME
        defaultInternationalStandarShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where name is not null
        defaultInternationalStandarShouldBeFound("name.specified=true");

        // Get all the internationalStandarList where name is null
        defaultInternationalStandarShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where status equals to DEFAULT_STATUS
        defaultInternationalStandarShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the internationalStandarList where status equals to UPDATED_STATUS
        defaultInternationalStandarShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInternationalStandarShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the internationalStandarList where status equals to UPDATED_STATUS
        defaultInternationalStandarShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInternationalStandarsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        internationalStandarRepository.saveAndFlush(internationalStandar);

        // Get all the internationalStandarList where status is not null
        defaultInternationalStandarShouldBeFound("status.specified=true");

        // Get all the internationalStandarList where status is null
        defaultInternationalStandarShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInternationalStandarShouldBeFound(String filter) throws Exception {
        restInternationalStandarMockMvc.perform(get("/api/international-standars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalStandar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInternationalStandarShouldNotBeFound(String filter) throws Exception {
        restInternationalStandarMockMvc.perform(get("/api/international-standars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInternationalStandar() throws Exception {
        // Get the internationalStandar
        restInternationalStandarMockMvc.perform(get("/api/international-standars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternationalStandar() throws Exception {
        // Initialize the database
        internationalStandarService.save(internationalStandar);

        int databaseSizeBeforeUpdate = internationalStandarRepository.findAll().size();

        // Update the internationalStandar
        InternationalStandar updatedInternationalStandar = internationalStandarRepository.findOne(internationalStandar.getId());
        // Disconnect from session so that the updates on updatedInternationalStandar are not directly saved in db
        em.detach(updatedInternationalStandar);
        updatedInternationalStandar
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);

        restInternationalStandarMockMvc.perform(put("/api/international-standars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInternationalStandar)))
            .andExpect(status().isOk());

        // Validate the InternationalStandar in the database
        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeUpdate);
        InternationalStandar testInternationalStandar = internationalStandarList.get(internationalStandarList.size() - 1);
        assertThat(testInternationalStandar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInternationalStandar.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingInternationalStandar() throws Exception {
        int databaseSizeBeforeUpdate = internationalStandarRepository.findAll().size();

        // Create the InternationalStandar

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternationalStandarMockMvc.perform(put("/api/international-standars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internationalStandar)))
            .andExpect(status().isCreated());

        // Validate the InternationalStandar in the database
        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInternationalStandar() throws Exception {
        // Initialize the database
        internationalStandarService.save(internationalStandar);

        int databaseSizeBeforeDelete = internationalStandarRepository.findAll().size();

        // Get the internationalStandar
        restInternationalStandarMockMvc.perform(delete("/api/international-standars/{id}", internationalStandar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InternationalStandar> internationalStandarList = internationalStandarRepository.findAll();
        assertThat(internationalStandarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternationalStandar.class);
        InternationalStandar internationalStandar1 = new InternationalStandar();
        internationalStandar1.setId(1L);
        InternationalStandar internationalStandar2 = new InternationalStandar();
        internationalStandar2.setId(internationalStandar1.getId());
        assertThat(internationalStandar1).isEqualTo(internationalStandar2);
        internationalStandar2.setId(2L);
        assertThat(internationalStandar1).isNotEqualTo(internationalStandar2);
        internationalStandar1.setId(null);
        assertThat(internationalStandar1).isNotEqualTo(internationalStandar2);
    }
}
