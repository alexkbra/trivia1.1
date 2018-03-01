package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Expedicion;
import co.com.elpoli.domain.Cuestionario;
import co.com.elpoli.repository.ExpedicionRepository;
import co.com.elpoli.service.ExpedicionService;
import co.com.elpoli.repository.search.ExpedicionSearchRepository;
import co.com.elpoli.service.dto.ExpedicionDTO;
import co.com.elpoli.service.mapper.ExpedicionMapper;
import co.com.elpoli.web.rest.errors.ExceptionTranslator;

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
 * Test class for the ExpedicionResource REST controller.
 *
 * @see ExpedicionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class ExpedicionResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private ExpedicionRepository expedicionRepository;

    @Autowired
    private ExpedicionMapper expedicionMapper;

    @Autowired
    private ExpedicionService expedicionService;

    @Autowired
    private ExpedicionSearchRepository expedicionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExpedicionMockMvc;

    private Expedicion expedicion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExpedicionResource expedicionResource = new ExpedicionResource(expedicionService);
        this.restExpedicionMockMvc = MockMvcBuilders.standaloneSetup(expedicionResource)
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
    public static Expedicion createEntity(EntityManager em) {
        Expedicion expedicion = new Expedicion()
            .nombre(DEFAULT_NOMBRE);
        // Add required entity
        Cuestionario cuestionario = CuestionarioResourceIntTest.createEntity(em);
        em.persist(cuestionario);
        em.flush();
        expedicion.getCuestionarios().add(cuestionario);
        return expedicion;
    }

    @Before
    public void initTest() {
        expedicionSearchRepository.deleteAll();
        expedicion = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpedicion() throws Exception {
        int databaseSizeBeforeCreate = expedicionRepository.findAll().size();

        // Create the Expedicion
        ExpedicionDTO expedicionDTO = expedicionMapper.toDto(expedicion);
        restExpedicionMockMvc.perform(post("/api/expedicions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expedicionDTO)))
            .andExpect(status().isCreated());

        // Validate the Expedicion in the database
        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeCreate + 1);
        Expedicion testExpedicion = expedicionList.get(expedicionList.size() - 1);
        assertThat(testExpedicion.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the Expedicion in Elasticsearch
        Expedicion expedicionEs = expedicionSearchRepository.findOne(testExpedicion.getId());
        assertThat(expedicionEs).isEqualToComparingFieldByField(testExpedicion);
    }

    @Test
    @Transactional
    public void createExpedicionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expedicionRepository.findAll().size();

        // Create the Expedicion with an existing ID
        expedicion.setId(1L);
        ExpedicionDTO expedicionDTO = expedicionMapper.toDto(expedicion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpedicionMockMvc.perform(post("/api/expedicions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expedicionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expedicion in the database
        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = expedicionRepository.findAll().size();
        // set the field null
        expedicion.setNombre(null);

        // Create the Expedicion, which fails.
        ExpedicionDTO expedicionDTO = expedicionMapper.toDto(expedicion);

        restExpedicionMockMvc.perform(post("/api/expedicions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expedicionDTO)))
            .andExpect(status().isBadRequest());

        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpedicions() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

        // Get all the expedicionList
        restExpedicionMockMvc.perform(get("/api/expedicions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expedicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

        // Get the expedicion
        restExpedicionMockMvc.perform(get("/api/expedicions/{id}", expedicion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expedicion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpedicion() throws Exception {
        // Get the expedicion
        restExpedicionMockMvc.perform(get("/api/expedicions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);
        expedicionSearchRepository.save(expedicion);
        int databaseSizeBeforeUpdate = expedicionRepository.findAll().size();

        // Update the expedicion
        Expedicion updatedExpedicion = expedicionRepository.findOne(expedicion.getId());
        updatedExpedicion
            .nombre(UPDATED_NOMBRE);
        ExpedicionDTO expedicionDTO = expedicionMapper.toDto(updatedExpedicion);

        restExpedicionMockMvc.perform(put("/api/expedicions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expedicionDTO)))
            .andExpect(status().isOk());

        // Validate the Expedicion in the database
        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeUpdate);
        Expedicion testExpedicion = expedicionList.get(expedicionList.size() - 1);
        assertThat(testExpedicion.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the Expedicion in Elasticsearch
        Expedicion expedicionEs = expedicionSearchRepository.findOne(testExpedicion.getId());
        assertThat(expedicionEs).isEqualToComparingFieldByField(testExpedicion);
    }

    @Test
    @Transactional
    public void updateNonExistingExpedicion() throws Exception {
        int databaseSizeBeforeUpdate = expedicionRepository.findAll().size();

        // Create the Expedicion
        ExpedicionDTO expedicionDTO = expedicionMapper.toDto(expedicion);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExpedicionMockMvc.perform(put("/api/expedicions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(expedicionDTO)))
            .andExpect(status().isCreated());

        // Validate the Expedicion in the database
        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);
        expedicionSearchRepository.save(expedicion);
        int databaseSizeBeforeDelete = expedicionRepository.findAll().size();

        // Get the expedicion
        restExpedicionMockMvc.perform(delete("/api/expedicions/{id}", expedicion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean expedicionExistsInEs = expedicionSearchRepository.exists(expedicion.getId());
        assertThat(expedicionExistsInEs).isFalse();

        // Validate the database is empty
        List<Expedicion> expedicionList = expedicionRepository.findAll();
        assertThat(expedicionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);
        expedicionSearchRepository.save(expedicion);

        // Search the expedicion
        restExpedicionMockMvc.perform(get("/api/_search/expedicions?query=id:" + expedicion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expedicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expedicion.class);
        Expedicion expedicion1 = new Expedicion();
        expedicion1.setId(1L);
        Expedicion expedicion2 = new Expedicion();
        expedicion2.setId(expedicion1.getId());
        assertThat(expedicion1).isEqualTo(expedicion2);
        expedicion2.setId(2L);
        assertThat(expedicion1).isNotEqualTo(expedicion2);
        expedicion1.setId(null);
        assertThat(expedicion1).isNotEqualTo(expedicion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpedicionDTO.class);
        ExpedicionDTO expedicionDTO1 = new ExpedicionDTO();
        expedicionDTO1.setId(1L);
        ExpedicionDTO expedicionDTO2 = new ExpedicionDTO();
        assertThat(expedicionDTO1).isNotEqualTo(expedicionDTO2);
        expedicionDTO2.setId(expedicionDTO1.getId());
        assertThat(expedicionDTO1).isEqualTo(expedicionDTO2);
        expedicionDTO2.setId(2L);
        assertThat(expedicionDTO1).isNotEqualTo(expedicionDTO2);
        expedicionDTO1.setId(null);
        assertThat(expedicionDTO1).isNotEqualTo(expedicionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(expedicionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(expedicionMapper.fromId(null)).isNull();
    }
}
