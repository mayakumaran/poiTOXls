package com.bytatech.web.rest;

import com.bytatech.ElasticbatchApp;

import com.bytatech.domain.Drivo;
import com.bytatech.repository.DrivoRepository;
import com.bytatech.repository.search.DrivoSearchRepository;
import com.bytatech.service.DrivoService;
import com.bytatech.service.dto.DrivoDTO;
import com.bytatech.service.mapper.DrivoMapper;
import com.bytatech.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.bytatech.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DrivoResource REST controller.
 *
 * @see DrivoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticbatchApp.class)
public class DrivoResourceIntTest {

    private static final String DEFAULT_REG_NO = "AAAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VEHDECSCR = "AAAAAAAAAA";
    private static final String UPDATED_VEHDECSCR = "BBBBBBBBBB";

    @Autowired
    private DrivoRepository drivoRepository;

    @Autowired
    private DrivoMapper drivoMapper;

    @Autowired
    private DrivoService drivoService;

    /**
     * This repository is mocked in the com.bytatech.repository.search test package.
     *
     * @see com.bytatech.repository.search.DrivoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DrivoSearchRepository mockDrivoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDrivoMockMvc;

    private Drivo drivo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DrivoResource drivoResource = new DrivoResource(drivoService);
        this.restDrivoMockMvc = MockMvcBuilders.standaloneSetup(drivoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drivo createEntity(EntityManager em) {
        Drivo drivo = new Drivo()
            .regNo(DEFAULT_REG_NO)
            .ownerName(DEFAULT_OWNER_NAME)
            .mobileNo(DEFAULT_MOBILE_NO)
            .vehdecscr(DEFAULT_VEHDECSCR);
        return drivo;
    }

    @Before
    public void initTest() {
        drivo = createEntity(em);
    }

    @Test
    @Transactional
    public void createDrivo() throws Exception {
        int databaseSizeBeforeCreate = drivoRepository.findAll().size();

        // Create the Drivo
        DrivoDTO drivoDTO = drivoMapper.toDto(drivo);
        restDrivoMockMvc.perform(post("/api/drivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Drivo in the database
        List<Drivo> drivoList = drivoRepository.findAll();
        assertThat(drivoList).hasSize(databaseSizeBeforeCreate + 1);
        Drivo testDrivo = drivoList.get(drivoList.size() - 1);
        assertThat(testDrivo.getRegNo()).isEqualTo(DEFAULT_REG_NO);
        assertThat(testDrivo.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testDrivo.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testDrivo.getVehdecscr()).isEqualTo(DEFAULT_VEHDECSCR);

        // Validate the Drivo in Elasticsearch
        verify(mockDrivoSearchRepository, times(1)).save(testDrivo);
    }

    @Test
    @Transactional
    public void createDrivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = drivoRepository.findAll().size();

        // Create the Drivo with an existing ID
        drivo.setId(1L);
        DrivoDTO drivoDTO = drivoMapper.toDto(drivo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDrivoMockMvc.perform(post("/api/drivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Drivo in the database
        List<Drivo> drivoList = drivoRepository.findAll();
        assertThat(drivoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Drivo in Elasticsearch
        verify(mockDrivoSearchRepository, times(0)).save(drivo);
    }

    @Test
    @Transactional
    public void getAllDrivos() throws Exception {
        // Initialize the database
        drivoRepository.saveAndFlush(drivo);

        // Get all the drivoList
        restDrivoMockMvc.perform(get("/api/drivos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO.toString())))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.toString())))
            .andExpect(jsonPath("$.[*].vehdecscr").value(hasItem(DEFAULT_VEHDECSCR.toString())));
    }
    
    @Test
    @Transactional
    public void getDrivo() throws Exception {
        // Initialize the database
        drivoRepository.saveAndFlush(drivo);

        // Get the drivo
        restDrivoMockMvc.perform(get("/api/drivos/{id}", drivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(drivo.getId().intValue()))
            .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO.toString()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME.toString()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.toString()))
            .andExpect(jsonPath("$.vehdecscr").value(DEFAULT_VEHDECSCR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDrivo() throws Exception {
        // Get the drivo
        restDrivoMockMvc.perform(get("/api/drivos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDrivo() throws Exception {
        // Initialize the database
        drivoRepository.saveAndFlush(drivo);

        int databaseSizeBeforeUpdate = drivoRepository.findAll().size();

        // Update the drivo
        Drivo updatedDrivo = drivoRepository.findById(drivo.getId()).get();
        // Disconnect from session so that the updates on updatedDrivo are not directly saved in db
        em.detach(updatedDrivo);
        updatedDrivo
            .regNo(UPDATED_REG_NO)
            .ownerName(UPDATED_OWNER_NAME)
            .mobileNo(UPDATED_MOBILE_NO)
            .vehdecscr(UPDATED_VEHDECSCR);
        DrivoDTO drivoDTO = drivoMapper.toDto(updatedDrivo);

        restDrivoMockMvc.perform(put("/api/drivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDTO)))
            .andExpect(status().isOk());

        // Validate the Drivo in the database
        List<Drivo> drivoList = drivoRepository.findAll();
        assertThat(drivoList).hasSize(databaseSizeBeforeUpdate);
        Drivo testDrivo = drivoList.get(drivoList.size() - 1);
        assertThat(testDrivo.getRegNo()).isEqualTo(UPDATED_REG_NO);
        assertThat(testDrivo.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testDrivo.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testDrivo.getVehdecscr()).isEqualTo(UPDATED_VEHDECSCR);

        // Validate the Drivo in Elasticsearch
        verify(mockDrivoSearchRepository, times(1)).save(testDrivo);
    }

    @Test
    @Transactional
    public void updateNonExistingDrivo() throws Exception {
        int databaseSizeBeforeUpdate = drivoRepository.findAll().size();

        // Create the Drivo
        DrivoDTO drivoDTO = drivoMapper.toDto(drivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrivoMockMvc.perform(put("/api/drivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Drivo in the database
        List<Drivo> drivoList = drivoRepository.findAll();
        assertThat(drivoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Drivo in Elasticsearch
        verify(mockDrivoSearchRepository, times(0)).save(drivo);
    }

    @Test
    @Transactional
    public void deleteDrivo() throws Exception {
        // Initialize the database
        drivoRepository.saveAndFlush(drivo);

        int databaseSizeBeforeDelete = drivoRepository.findAll().size();

        // Delete the drivo
        restDrivoMockMvc.perform(delete("/api/drivos/{id}", drivo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Drivo> drivoList = drivoRepository.findAll();
        assertThat(drivoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Drivo in Elasticsearch
        verify(mockDrivoSearchRepository, times(1)).deleteById(drivo.getId());
    }

    @Test
    @Transactional
    public void searchDrivo() throws Exception {
        // Initialize the database
        drivoRepository.saveAndFlush(drivo);
        when(mockDrivoSearchRepository.search(queryStringQuery("id:" + drivo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(drivo), PageRequest.of(0, 1), 1));
        // Search the drivo
        restDrivoMockMvc.perform(get("/api/_search/drivos?query=id:" + drivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].vehdecscr").value(hasItem(DEFAULT_VEHDECSCR)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Drivo.class);
        Drivo drivo1 = new Drivo();
        drivo1.setId(1L);
        Drivo drivo2 = new Drivo();
        drivo2.setId(drivo1.getId());
        assertThat(drivo1).isEqualTo(drivo2);
        drivo2.setId(2L);
        assertThat(drivo1).isNotEqualTo(drivo2);
        drivo1.setId(null);
        assertThat(drivo1).isNotEqualTo(drivo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrivoDTO.class);
        DrivoDTO drivoDTO1 = new DrivoDTO();
        drivoDTO1.setId(1L);
        DrivoDTO drivoDTO2 = new DrivoDTO();
        assertThat(drivoDTO1).isNotEqualTo(drivoDTO2);
        drivoDTO2.setId(drivoDTO1.getId());
        assertThat(drivoDTO1).isEqualTo(drivoDTO2);
        drivoDTO2.setId(2L);
        assertThat(drivoDTO1).isNotEqualTo(drivoDTO2);
        drivoDTO1.setId(null);
        assertThat(drivoDTO1).isNotEqualTo(drivoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(drivoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(drivoMapper.fromId(null)).isNull();
    }
}
