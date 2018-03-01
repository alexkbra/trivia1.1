package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Cuestionario;
import co.com.elpoli.domain.Regla;
import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.domain.Expedicion;
import co.com.elpoli.repository.CuestionarioRepository;
import co.com.elpoli.service.CuestionarioService;
import co.com.elpoli.repository.search.CuestionarioSearchRepository;
import co.com.elpoli.service.dto.CuestionarioDTO;
import co.com.elpoli.service.mapper.CuestionarioMapper;
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
 * Test class for the CuestionarioResource REST controller.
 *
 * @see CuestionarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class CuestionarioResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    @Autowired
    private CuestionarioMapper cuestionarioMapper;

    @Autowired
    private CuestionarioService cuestionarioService;

    @Autowired
    private CuestionarioSearchRepository cuestionarioSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCuestionarioMockMvc;

    private Cuestionario cuestionario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CuestionarioResource cuestionarioResource = new CuestionarioResource(cuestionarioService);
        this.restCuestionarioMockMvc = MockMvcBuilders.standaloneSetup(cuestionarioResource)
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
    public static Cuestionario createEntity(EntityManager em) {
        Cuestionario cuestionario = new Cuestionario()
            .nombre(DEFAULT_NOMBRE);
        // Add required entity
        Regla regla = ReglaResourceIntTest.createEntity(em);
        em.persist(regla);
        em.flush();
        cuestionario.getReglas().add(regla);
        // Add required entity
        Pregunta pregunta = PreguntaResourceIntTest.createEntity(em);
        em.persist(pregunta);
        em.flush();
        cuestionario.getPreguntas().add(pregunta);
        // Add required entity
        Expedicion expedicion = ExpedicionResourceIntTest.createEntity(em);
        em.persist(expedicion);
        em.flush();
        cuestionario.setExpedicion(expedicion);
        return cuestionario;
    }

    @Before
    public void initTest() {
        cuestionarioSearchRepository.deleteAll();
        cuestionario = createEntity(em);
    }

    @Test
    @Transactional
    public void createCuestionario() throws Exception {
        int databaseSizeBeforeCreate = cuestionarioRepository.findAll().size();

        // Create the Cuestionario
        CuestionarioDTO cuestionarioDTO = cuestionarioMapper.toDto(cuestionario);
        restCuestionarioMockMvc.perform(post("/api/cuestionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuestionarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Cuestionario in the database
        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeCreate + 1);
        Cuestionario testCuestionario = cuestionarioList.get(cuestionarioList.size() - 1);
        assertThat(testCuestionario.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the Cuestionario in Elasticsearch
        Cuestionario cuestionarioEs = cuestionarioSearchRepository.findOne(testCuestionario.getId());
        assertThat(cuestionarioEs).isEqualToComparingFieldByField(testCuestionario);
    }

    @Test
    @Transactional
    public void createCuestionarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cuestionarioRepository.findAll().size();

        // Create the Cuestionario with an existing ID
        cuestionario.setId(1L);
        CuestionarioDTO cuestionarioDTO = cuestionarioMapper.toDto(cuestionario);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuestionarioMockMvc.perform(post("/api/cuestionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuestionarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cuestionario in the database
        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuestionarioRepository.findAll().size();
        // set the field null
        cuestionario.setNombre(null);

        // Create the Cuestionario, which fails.
        CuestionarioDTO cuestionarioDTO = cuestionarioMapper.toDto(cuestionario);

        restCuestionarioMockMvc.perform(post("/api/cuestionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuestionarioDTO)))
            .andExpect(status().isBadRequest());

        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCuestionarios() throws Exception {
        // Initialize the database
        cuestionarioRepository.saveAndFlush(cuestionario);

        // Get all the cuestionarioList
        restCuestionarioMockMvc.perform(get("/api/cuestionarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuestionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getCuestionario() throws Exception {
        // Initialize the database
        cuestionarioRepository.saveAndFlush(cuestionario);

        // Get the cuestionario
        restCuestionarioMockMvc.perform(get("/api/cuestionarios/{id}", cuestionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cuestionario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCuestionario() throws Exception {
        // Get the cuestionario
        restCuestionarioMockMvc.perform(get("/api/cuestionarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCuestionario() throws Exception {
        // Initialize the database
        cuestionarioRepository.saveAndFlush(cuestionario);
        cuestionarioSearchRepository.save(cuestionario);
        int databaseSizeBeforeUpdate = cuestionarioRepository.findAll().size();

        // Update the cuestionario
        Cuestionario updatedCuestionario = cuestionarioRepository.findOne(cuestionario.getId());
        updatedCuestionario
            .nombre(UPDATED_NOMBRE);
        CuestionarioDTO cuestionarioDTO = cuestionarioMapper.toDto(updatedCuestionario);

        restCuestionarioMockMvc.perform(put("/api/cuestionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuestionarioDTO)))
            .andExpect(status().isOk());

        // Validate the Cuestionario in the database
        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeUpdate);
        Cuestionario testCuestionario = cuestionarioList.get(cuestionarioList.size() - 1);
        assertThat(testCuestionario.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the Cuestionario in Elasticsearch
        Cuestionario cuestionarioEs = cuestionarioSearchRepository.findOne(testCuestionario.getId());
        assertThat(cuestionarioEs).isEqualToComparingFieldByField(testCuestionario);
    }

    @Test
    @Transactional
    public void updateNonExistingCuestionario() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioRepository.findAll().size();

        // Create the Cuestionario
        CuestionarioDTO cuestionarioDTO = cuestionarioMapper.toDto(cuestionario);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCuestionarioMockMvc.perform(put("/api/cuestionarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuestionarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Cuestionario in the database
        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCuestionario() throws Exception {
        // Initialize the database
        cuestionarioRepository.saveAndFlush(cuestionario);
        cuestionarioSearchRepository.save(cuestionario);
        int databaseSizeBeforeDelete = cuestionarioRepository.findAll().size();

        // Get the cuestionario
        restCuestionarioMockMvc.perform(delete("/api/cuestionarios/{id}", cuestionario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cuestionarioExistsInEs = cuestionarioSearchRepository.exists(cuestionario.getId());
        assertThat(cuestionarioExistsInEs).isFalse();

        // Validate the database is empty
        List<Cuestionario> cuestionarioList = cuestionarioRepository.findAll();
        assertThat(cuestionarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCuestionario() throws Exception {
        // Initialize the database
        cuestionarioRepository.saveAndFlush(cuestionario);
        cuestionarioSearchRepository.save(cuestionario);

        // Search the cuestionario
        restCuestionarioMockMvc.perform(get("/api/_search/cuestionarios?query=id:" + cuestionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuestionario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuestionario.class);
        Cuestionario cuestionario1 = new Cuestionario();
        cuestionario1.setId(1L);
        Cuestionario cuestionario2 = new Cuestionario();
        cuestionario2.setId(cuestionario1.getId());
        assertThat(cuestionario1).isEqualTo(cuestionario2);
        cuestionario2.setId(2L);
        assertThat(cuestionario1).isNotEqualTo(cuestionario2);
        cuestionario1.setId(null);
        assertThat(cuestionario1).isNotEqualTo(cuestionario2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuestionarioDTO.class);
        CuestionarioDTO cuestionarioDTO1 = new CuestionarioDTO();
        cuestionarioDTO1.setId(1L);
        CuestionarioDTO cuestionarioDTO2 = new CuestionarioDTO();
        assertThat(cuestionarioDTO1).isNotEqualTo(cuestionarioDTO2);
        cuestionarioDTO2.setId(cuestionarioDTO1.getId());
        assertThat(cuestionarioDTO1).isEqualTo(cuestionarioDTO2);
        cuestionarioDTO2.setId(2L);
        assertThat(cuestionarioDTO1).isNotEqualTo(cuestionarioDTO2);
        cuestionarioDTO1.setId(null);
        assertThat(cuestionarioDTO1).isNotEqualTo(cuestionarioDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cuestionarioMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cuestionarioMapper.fromId(null)).isNull();
    }
}
