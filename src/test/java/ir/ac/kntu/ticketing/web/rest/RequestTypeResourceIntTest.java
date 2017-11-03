package ir.ac.kntu.ticketing.web.rest;

import ir.ac.kntu.ticketing.TicketingSystemApp;

import ir.ac.kntu.ticketing.domain.RequestType;
import ir.ac.kntu.ticketing.repository.RequestTypeRepository;
import ir.ac.kntu.ticketing.service.RequestTypeService;
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
 * Test class for the RequestTypeResource REST controller.
 *
 * @see RequestTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class RequestTypeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private RequestTypeService requestTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRequestTypeMockMvc;

    private RequestType requestType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequestTypeResource requestTypeResource = new RequestTypeResource(requestTypeService);
        this.restRequestTypeMockMvc = MockMvcBuilders.standaloneSetup(requestTypeResource)
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
    public static RequestType createEntity(EntityManager em) {
        RequestType requestType = new RequestType()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE);
        return requestType;
    }

    @Before
    public void initTest() {
        requestType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestType() throws Exception {
        int databaseSizeBeforeCreate = requestTypeRepository.findAll().size();

        // Create the RequestType
        restRequestTypeMockMvc.perform(post("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isCreated());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRequestType.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createRequestTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestTypeRepository.findAll().size();

        // Create the RequestType with an existing ID
        requestType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestTypeMockMvc.perform(post("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().size();
        // set the field null
        requestType.setCode(null);

        // Create the RequestType, which fails.

        restRequestTypeMockMvc.perform(post("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().size();
        // set the field null
        requestType.setTitle(null);

        // Create the RequestType, which fails.

        restRequestTypeMockMvc.perform(post("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequestTypes() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        // Get all the requestTypeList
        restRequestTypeMockMvc.perform(get("/api/request-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getRequestType() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        // Get the requestType
        restRequestTypeMockMvc.perform(get("/api/request-types/{id}", requestType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requestType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequestType() throws Exception {
        // Get the requestType
        restRequestTypeMockMvc.perform(get("/api/request-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestType() throws Exception {
        // Initialize the database
        requestTypeService.save(requestType);

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();

        // Update the requestType
        RequestType updatedRequestType = requestTypeRepository.findOne(requestType.getId());
        updatedRequestType
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE);

        restRequestTypeMockMvc.perform(put("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequestType)))
            .andExpect(status().isOk());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRequestType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();

        // Create the RequestType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequestTypeMockMvc.perform(put("/api/request-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isCreated());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequestType() throws Exception {
        // Initialize the database
        requestTypeService.save(requestType);

        int databaseSizeBeforeDelete = requestTypeRepository.findAll().size();

        // Get the requestType
        restRequestTypeMockMvc.perform(delete("/api/request-types/{id}", requestType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestType.class);
        RequestType requestType1 = new RequestType();
        requestType1.setId(1L);
        RequestType requestType2 = new RequestType();
        requestType2.setId(requestType1.getId());
        assertThat(requestType1).isEqualTo(requestType2);
        requestType2.setId(2L);
        assertThat(requestType1).isNotEqualTo(requestType2);
        requestType1.setId(null);
        assertThat(requestType1).isNotEqualTo(requestType2);
    }
}
