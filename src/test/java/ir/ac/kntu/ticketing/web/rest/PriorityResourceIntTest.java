package ir.ac.kntu.ticketing.web.rest;

import ir.ac.kntu.ticketing.TicketingSystemApp;

import ir.ac.kntu.ticketing.domain.Priority;
import ir.ac.kntu.ticketing.repository.PriorityRepository;
import ir.ac.kntu.ticketing.service.PriorityService;
import ir.ac.kntu.ticketing.web.rest.errors.ExceptionTranslator;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PriorityResource REST controller.
 *
 * @see PriorityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class PriorityResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPriorityMockMvc;

    private Priority priority;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PriorityResource priorityResource = new PriorityResource(priorityService);
        this.restPriorityMockMvc = MockMvcBuilders.standaloneSetup(priorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Priority createEntity(EntityManager em) {
        Priority priority = new Priority()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE);
        return priority;
    }

    @Before
    public void initTest() {
        priority = createEntity(em);
    }

    @Test
    @Transactional
    public void createPriority() throws Exception {
        int databaseSizeBeforeCreate = priorityRepository.findAll().size();

        // Create the Priority
        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priority)))
            .andExpect(status().isCreated());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate + 1);
        Priority testPriority = priorityList.get(priorityList.size() - 1);
        assertThat(testPriority.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPriority.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createPriorityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = priorityRepository.findAll().size();

        // Create the Priority with an existing ID
        priority.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priority)))
            .andExpect(status().isBadRequest());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = priorityRepository.findAll().size();
        // set the field null
        priority.setCode(null);

        // Create the Priority, which fails.

        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priority)))
            .andExpect(status().isBadRequest());

        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = priorityRepository.findAll().size();
        // set the field null
        priority.setTitle(null);

        // Create the Priority, which fails.

        restPriorityMockMvc.perform(post("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priority)))
            .andExpect(status().isBadRequest());

        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPriorities() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get all the priorityList
        restPriorityMockMvc.perform(get("/api/priorities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priority.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getPriority() throws Exception {
        // Initialize the database
        priorityRepository.saveAndFlush(priority);

        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", priority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(priority.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPriority() throws Exception {
        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriority() throws Exception {
        // Initialize the database
        priorityService.save(priority);

        int databaseSizeBeforeUpdate = priorityRepository.findAll().size();

        // Update the priority
        Priority updatedPriority = priorityRepository.findOne(priority.getId());
        updatedPriority
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE);

        restPriorityMockMvc.perform(put("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPriority)))
            .andExpect(status().isOk());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate);
        Priority testPriority = priorityList.get(priorityList.size() - 1);
        assertThat(testPriority.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPriority.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingPriority() throws Exception {
        int databaseSizeBeforeUpdate = priorityRepository.findAll().size();

        // Create the Priority

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPriorityMockMvc.perform(put("/api/priorities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(priority)))
            .andExpect(status().isCreated());

        // Validate the Priority in the database
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePriority() throws Exception {
        // Initialize the database
        priorityService.save(priority);

        int databaseSizeBeforeDelete = priorityRepository.findAll().size();

        // Get the priority
        restPriorityMockMvc.perform(delete("/api/priorities/{id}", priority.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Priority> priorityList = priorityRepository.findAll();
        assertThat(priorityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Priority.class);
        Priority priority1 = new Priority();
        priority1.setId(1L);
        Priority priority2 = new Priority();
        priority2.setId(priority1.getId());
        assertThat(priority1).isEqualTo(priority2);
        priority2.setId(2L);
        assertThat(priority1).isNotEqualTo(priority2);
        priority1.setId(null);
        assertThat(priority1).isNotEqualTo(priority2);
    }
}
