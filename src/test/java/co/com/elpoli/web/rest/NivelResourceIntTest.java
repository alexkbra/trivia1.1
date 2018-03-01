package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Nivel;
import co.com.elpoli.repository.NivelRepository;
import co.com.elpoli.service.NivelService;
import co.com.elpoli.repository.search.NivelSearchRepository;
import co.com.elpoli.service.dto.NivelDTO;
import co.com.elpoli.service.mapper.NivelMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NivelResource REST controller.
 *
 * @see NivelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class NivelResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FINAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FINAL = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMERO_PREGUNTAS = 1;
    private static final Integer UPDATED_NUMERO_PREGUNTAS = 2;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private NivelMapper nivelMapper;

    @Autowired
    private NivelService nivelService;

    @Autowired
    private NivelSearchRepository nivelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNivelMockMvc;

    private Nivel nivel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NivelResource nivelResource = new NivelResource(nivelService);
        this.restNivelMockMvc = MockMvcBuilders.standaloneSetup(nivelResource)
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
    public static Nivel createEntity(EntityManager em) {
        Nivel nivel = new Nivel()
            .nombre(DEFAULT_NOMBRE)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFinal(DEFAULT_FECHA_FINAL)
            .numeroPreguntas(DEFAULT_NUMERO_PREGUNTAS);
        return nivel;
    }

    @Before
    public void initTest() {
        nivelSearchRepository.deleteAll();
        nivel = createEntity(em);
    }

    @Test
    @Transactional
    public void createNivel() throws Exception {
        int databaseSizeBeforeCreate = nivelRepository.findAll().size();

        // Create the Nivel
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);
        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isCreated());

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeCreate + 1);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testNivel.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testNivel.getFechaFinal()).isEqualTo(DEFAULT_FECHA_FINAL);
        assertThat(testNivel.getNumeroPreguntas()).isEqualTo(DEFAULT_NUMERO_PREGUNTAS);

        // Validate the Nivel in Elasticsearch
        Nivel nivelEs = nivelSearchRepository.findOne(testNivel.getId());
        assertThat(nivelEs).isEqualToComparingFieldByField(testNivel);
    }

    @Test
    @Transactional
    public void createNivelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nivelRepository.findAll().size();

        // Create the Nivel with an existing ID
        nivel.setId(1L);
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = nivelRepository.findAll().size();
        // set the field null
        nivel.setNombre(null);

        // Create the Nivel, which fails.
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isBadRequest());

        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = nivelRepository.findAll().size();
        // set the field null
        nivel.setFechaInicio(null);

        // Create the Nivel, which fails.
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isBadRequest());

        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaFinalIsRequired() throws Exception {
        int databaseSizeBeforeTest = nivelRepository.findAll().size();
        // set the field null
        nivel.setFechaFinal(null);

        // Create the Nivel, which fails.
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isBadRequest());

        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroPreguntasIsRequired() throws Exception {
        int databaseSizeBeforeTest = nivelRepository.findAll().size();
        // set the field null
        nivel.setNumeroPreguntas(null);

        // Create the Nivel, which fails.
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        restNivelMockMvc.perform(post("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isBadRequest());

        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNivels() throws Exception {
        // Initialize the database
        nivelRepository.saveAndFlush(nivel);

        // Get all the nivelList
        restNivelMockMvc.perform(get("/api/nivels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nivel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFinal").value(hasItem(DEFAULT_FECHA_FINAL.toString())))
            .andExpect(jsonPath("$.[*].numeroPreguntas").value(hasItem(DEFAULT_NUMERO_PREGUNTAS)));
    }

    @Test
    @Transactional
    public void getNivel() throws Exception {
        // Initialize the database
        nivelRepository.saveAndFlush(nivel);

        // Get the nivel
        restNivelMockMvc.perform(get("/api/nivels/{id}", nivel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nivel.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFinal").value(DEFAULT_FECHA_FINAL.toString()))
            .andExpect(jsonPath("$.numeroPreguntas").value(DEFAULT_NUMERO_PREGUNTAS));
    }

    @Test
    @Transactional
    public void getNonExistingNivel() throws Exception {
        // Get the nivel
        restNivelMockMvc.perform(get("/api/nivels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNivel() throws Exception {
        // Initialize the database
        nivelRepository.saveAndFlush(nivel);
        nivelSearchRepository.save(nivel);
        int databaseSizeBeforeUpdate = nivelRepository.findAll().size();

        // Update the nivel
        Nivel updatedNivel = nivelRepository.findOne(nivel.getId());
        updatedNivel
            .nombre(UPDATED_NOMBRE)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFinal(UPDATED_FECHA_FINAL)
            .numeroPreguntas(UPDATED_NUMERO_PREGUNTAS);
        NivelDTO nivelDTO = nivelMapper.toDto(updatedNivel);

        restNivelMockMvc.perform(put("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isOk());

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate);
        Nivel testNivel = nivelList.get(nivelList.size() - 1);
        assertThat(testNivel.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testNivel.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testNivel.getFechaFinal()).isEqualTo(UPDATED_FECHA_FINAL);
        assertThat(testNivel.getNumeroPreguntas()).isEqualTo(UPDATED_NUMERO_PREGUNTAS);

        // Validate the Nivel in Elasticsearch
        Nivel nivelEs = nivelSearchRepository.findOne(testNivel.getId());
        assertThat(nivelEs).isEqualToComparingFieldByField(testNivel);
    }

    @Test
    @Transactional
    public void updateNonExistingNivel() throws Exception {
        int databaseSizeBeforeUpdate = nivelRepository.findAll().size();

        // Create the Nivel
        NivelDTO nivelDTO = nivelMapper.toDto(nivel);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNivelMockMvc.perform(put("/api/nivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nivelDTO)))
            .andExpect(status().isCreated());

        // Validate the Nivel in the database
        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNivel() throws Exception {
        // Initialize the database
        nivelRepository.saveAndFlush(nivel);
        nivelSearchRepository.save(nivel);
        int databaseSizeBeforeDelete = nivelRepository.findAll().size();

        // Get the nivel
        restNivelMockMvc.perform(delete("/api/nivels/{id}", nivel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean nivelExistsInEs = nivelSearchRepository.exists(nivel.getId());
        assertThat(nivelExistsInEs).isFalse();

        // Validate the database is empty
        List<Nivel> nivelList = nivelRepository.findAll();
        assertThat(nivelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNivel() throws Exception {
        // Initialize the database
        nivelRepository.saveAndFlush(nivel);
        nivelSearchRepository.save(nivel);

        // Search the nivel
        restNivelMockMvc.perform(get("/api/_search/nivels?query=id:" + nivel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nivel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFinal").value(hasItem(DEFAULT_FECHA_FINAL.toString())))
            .andExpect(jsonPath("$.[*].numeroPreguntas").value(hasItem(DEFAULT_NUMERO_PREGUNTAS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nivel.class);
        Nivel nivel1 = new Nivel();
        nivel1.setId(1L);
        Nivel nivel2 = new Nivel();
        nivel2.setId(nivel1.getId());
        assertThat(nivel1).isEqualTo(nivel2);
        nivel2.setId(2L);
        assertThat(nivel1).isNotEqualTo(nivel2);
        nivel1.setId(null);
        assertThat(nivel1).isNotEqualTo(nivel2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NivelDTO.class);
        NivelDTO nivelDTO1 = new NivelDTO();
        nivelDTO1.setId(1L);
        NivelDTO nivelDTO2 = new NivelDTO();
        assertThat(nivelDTO1).isNotEqualTo(nivelDTO2);
        nivelDTO2.setId(nivelDTO1.getId());
        assertThat(nivelDTO1).isEqualTo(nivelDTO2);
        nivelDTO2.setId(2L);
        assertThat(nivelDTO1).isNotEqualTo(nivelDTO2);
        nivelDTO1.setId(null);
        assertThat(nivelDTO1).isNotEqualTo(nivelDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(nivelMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(nivelMapper.fromId(null)).isNull();
    }
}
