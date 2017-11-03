package ir.ac.kntu.ticketing.web.rest;

import ir.ac.kntu.ticketing.TicketingSystemApp;

import ir.ac.kntu.ticketing.domain.TaskType;
import ir.ac.kntu.ticketing.repository.TaskTypeRepository;
import ir.ac.kntu.ticketing.service.TaskTypeService;
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
 * Test class for the TaskTypeResource REST controller.
 *
 * @see TaskTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class TaskTypeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private TaskTypeRepository taskTypeRepository;

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskTypeMockMvc;

    private TaskType taskType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskTypeResource taskTypeResource = new TaskTypeResource(taskTypeService);
        this.restTaskTypeMockMvc = MockMvcBuilders.standaloneSetup(taskTypeResource)
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
    public static TaskType createEntity(EntityManager em) {
        TaskType taskType = new TaskType()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE);
        return taskType;
    }

    @Before
    public void initTest() {
        taskType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskType() throws Exception {
        int databaseSizeBeforeCreate = taskTypeRepository.findAll().size();

        // Create the TaskType
        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskType)))
            .andExpect(status().isCreated());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TaskType testTaskType = taskTypeList.get(taskTypeList.size() - 1);
        assertThat(testTaskType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTaskType.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createTaskTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskTypeRepository.findAll().size();

        // Create the TaskType with an existing ID
        taskType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskType)))
            .andExpect(status().isBadRequest());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTypeRepository.findAll().size();
        // set the field null
        taskType.setCode(null);

        // Create the TaskType, which fails.

        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskType)))
            .andExpect(status().isBadRequest());

        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTypeRepository.findAll().size();
        // set the field null
        taskType.setTitle(null);

        // Create the TaskType, which fails.

        restTaskTypeMockMvc.perform(post("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskType)))
            .andExpect(status().isBadRequest());

        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskTypes() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get all the taskTypeList
        restTaskTypeMockMvc.perform(get("/api/task-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getTaskType() throws Exception {
        // Initialize the database
        taskTypeRepository.saveAndFlush(taskType);

        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", taskType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskType() throws Exception {
        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskType() throws Exception {
        // Initialize the database
        taskTypeService.save(taskType);

        int databaseSizeBeforeUpdate = taskTypeRepository.findAll().size();

        // Update the taskType
        TaskType updatedTaskType = taskTypeRepository.findOne(taskType.getId());
        updatedTaskType
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE);

        restTaskTypeMockMvc.perform(put("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskType)))
            .andExpect(status().isOk());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate);
        TaskType testTaskType = taskTypeList.get(taskTypeList.size() - 1);
        assertThat(testTaskType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaskType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskType() throws Exception {
        int databaseSizeBeforeUpdate = taskTypeRepository.findAll().size();

        // Create the TaskType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskTypeMockMvc.perform(put("/api/task-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskType)))
            .andExpect(status().isCreated());

        // Validate the TaskType in the database
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskType() throws Exception {
        // Initialize the database
        taskTypeService.save(taskType);

        int databaseSizeBeforeDelete = taskTypeRepository.findAll().size();

        // Get the taskType
        restTaskTypeMockMvc.perform(delete("/api/task-types/{id}", taskType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskType> taskTypeList = taskTypeRepository.findAll();
        assertThat(taskTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskType.class);
        TaskType taskType1 = new TaskType();
        taskType1.setId(1L);
        TaskType taskType2 = new TaskType();
        taskType2.setId(taskType1.getId());
        assertThat(taskType1).isEqualTo(taskType2);
        taskType2.setId(2L);
        assertThat(taskType1).isNotEqualTo(taskType2);
        taskType1.setId(null);
        assertThat(taskType1).isNotEqualTo(taskType2);
    }
}
