package mx.gob.scjn.desca.web.rest;

import mx.gob.scjn.desca.DescaApp;

import mx.gob.scjn.desca.domain.Methodology;
import mx.gob.scjn.desca.repository.MethodologyRepository;
import mx.gob.scjn.desca.service.MethodologyService;
import mx.gob.scjn.desca.repository.search.MethodologySearchRepository;
import mx.gob.scjn.desca.service.dto.MethodologyDTO;
import mx.gob.scjn.desca.service.mapper.MethodologyMapper;
import mx.gob.scjn.desca.web.rest.errors.ExceptionTranslator;
import mx.gob.scjn.desca.service.dto.MethodologyCriteria;
import mx.gob.scjn.desca.service.MethodologyQueryService;

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
 * Test class for the MethodologyResource REST controller.
 *
 * @see MethodologyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DescaApp.class)
public class MethodologyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private MethodologyRepository methodologyRepository;

    @Autowired
    private MethodologyMapper methodologyMapper;

    @Autowired
    private MethodologyService methodologyService;

    @Autowired
    private MethodologySearchRepository methodologySearchRepository;

    @Autowired
    private MethodologyQueryService methodologyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMethodologyMockMvc;

    private Methodology methodology;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MethodologyResource methodologyResource = new MethodologyResource(methodologyService, methodologyQueryService);
        this.restMethodologyMockMvc = MockMvcBuilders.standaloneSetup(methodologyResource)
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
    public static Methodology createEntity(EntityManager em) {
        Methodology methodology = new Methodology()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return methodology;
    }

    @Before
    public void initTest() {
        methodologySearchRepository.deleteAll();
        methodology = createEntity(em);
    }

    @Test
    @Transactional
    public void createMethodology() throws Exception {
        int databaseSizeBeforeCreate = methodologyRepository.findAll().size();

        // Create the Methodology
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(methodology);
        restMethodologyMockMvc.perform(post("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isCreated());

        // Validate the Methodology in the database
        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeCreate + 1);
        Methodology testMethodology = methodologyList.get(methodologyList.size() - 1);
        assertThat(testMethodology.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMethodology.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Methodology in Elasticsearch
        Methodology methodologyEs = methodologySearchRepository.findOne(testMethodology.getId());
        assertThat(methodologyEs).isEqualToIgnoringGivenFields(testMethodology);
    }

    @Test
    @Transactional
    public void createMethodologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = methodologyRepository.findAll().size();

        // Create the Methodology with an existing ID
        methodology.setId(1L);
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(methodology);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMethodologyMockMvc.perform(post("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Methodology in the database
        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = methodologyRepository.findAll().size();
        // set the field null
        methodology.setName(null);

        // Create the Methodology, which fails.
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(methodology);

        restMethodologyMockMvc.perform(post("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isBadRequest());

        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = methodologyRepository.findAll().size();
        // set the field null
        methodology.setStatus(null);

        // Create the Methodology, which fails.
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(methodology);

        restMethodologyMockMvc.perform(post("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isBadRequest());

        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMethodologies() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList
        restMethodologyMockMvc.perform(get("/api/methodologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(methodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMethodology() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get the methodology
        restMethodologyMockMvc.perform(get("/api/methodologies/{id}", methodology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(methodology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMethodologiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where name equals to DEFAULT_NAME
        defaultMethodologyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the methodologyList where name equals to UPDATED_NAME
        defaultMethodologyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMethodologiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMethodologyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the methodologyList where name equals to UPDATED_NAME
        defaultMethodologyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMethodologiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where name is not null
        defaultMethodologyShouldBeFound("name.specified=true");

        // Get all the methodologyList where name is null
        defaultMethodologyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMethodologiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where status equals to DEFAULT_STATUS
        defaultMethodologyShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the methodologyList where status equals to UPDATED_STATUS
        defaultMethodologyShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMethodologiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMethodologyShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the methodologyList where status equals to UPDATED_STATUS
        defaultMethodologyShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMethodologiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);

        // Get all the methodologyList where status is not null
        defaultMethodologyShouldBeFound("status.specified=true");

        // Get all the methodologyList where status is null
        defaultMethodologyShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMethodologyShouldBeFound(String filter) throws Exception {
        restMethodologyMockMvc.perform(get("/api/methodologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(methodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMethodologyShouldNotBeFound(String filter) throws Exception {
        restMethodologyMockMvc.perform(get("/api/methodologies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMethodology() throws Exception {
        // Get the methodology
        restMethodologyMockMvc.perform(get("/api/methodologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMethodology() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);
        methodologySearchRepository.save(methodology);
        int databaseSizeBeforeUpdate = methodologyRepository.findAll().size();

        // Update the methodology
        Methodology updatedMethodology = methodologyRepository.findOne(methodology.getId());
        // Disconnect from session so that the updates on updatedMethodology are not directly saved in db
        em.detach(updatedMethodology);
        updatedMethodology
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(updatedMethodology);

        restMethodologyMockMvc.perform(put("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isOk());

        // Validate the Methodology in the database
        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeUpdate);
        Methodology testMethodology = methodologyList.get(methodologyList.size() - 1);
        assertThat(testMethodology.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMethodology.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Methodology in Elasticsearch
        Methodology methodologyEs = methodologySearchRepository.findOne(testMethodology.getId());
        assertThat(methodologyEs).isEqualToIgnoringGivenFields(testMethodology);
    }

    @Test
    @Transactional
    public void updateNonExistingMethodology() throws Exception {
        int databaseSizeBeforeUpdate = methodologyRepository.findAll().size();

        // Create the Methodology
        MethodologyDTO methodologyDTO = methodologyMapper.toDto(methodology);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMethodologyMockMvc.perform(put("/api/methodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(methodologyDTO)))
            .andExpect(status().isCreated());

        // Validate the Methodology in the database
        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMethodology() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);
        methodologySearchRepository.save(methodology);
        int databaseSizeBeforeDelete = methodologyRepository.findAll().size();

        // Get the methodology
        restMethodologyMockMvc.perform(delete("/api/methodologies/{id}", methodology.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean methodologyExistsInEs = methodologySearchRepository.exists(methodology.getId());
        assertThat(methodologyExistsInEs).isFalse();

        // Validate the database is empty
        List<Methodology> methodologyList = methodologyRepository.findAll();
        assertThat(methodologyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMethodology() throws Exception {
        // Initialize the database
        methodologyRepository.saveAndFlush(methodology);
        methodologySearchRepository.save(methodology);

        // Search the methodology
        restMethodologyMockMvc.perform(get("/api/_search/methodologies?query=id:" + methodology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(methodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Methodology.class);
        Methodology methodology1 = new Methodology();
        methodology1.setId(1L);
        Methodology methodology2 = new Methodology();
        methodology2.setId(methodology1.getId());
        assertThat(methodology1).isEqualTo(methodology2);
        methodology2.setId(2L);
        assertThat(methodology1).isNotEqualTo(methodology2);
        methodology1.setId(null);
        assertThat(methodology1).isNotEqualTo(methodology2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MethodologyDTO.class);
        MethodologyDTO methodologyDTO1 = new MethodologyDTO();
        methodologyDTO1.setId(1L);
        MethodologyDTO methodologyDTO2 = new MethodologyDTO();
        assertThat(methodologyDTO1).isNotEqualTo(methodologyDTO2);
        methodologyDTO2.setId(methodologyDTO1.getId());
        assertThat(methodologyDTO1).isEqualTo(methodologyDTO2);
        methodologyDTO2.setId(2L);
        assertThat(methodologyDTO1).isNotEqualTo(methodologyDTO2);
        methodologyDTO1.setId(null);
        assertThat(methodologyDTO1).isNotEqualTo(methodologyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(methodologyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(methodologyMapper.fromId(null)).isNull();
    }
}
