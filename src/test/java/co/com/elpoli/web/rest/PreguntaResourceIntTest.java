package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.domain.Respuesta;
import co.com.elpoli.domain.Pista;
import co.com.elpoli.domain.Cuestionario;
import co.com.elpoli.domain.TipoPregunta;
import co.com.elpoli.repository.PreguntaRepository;
import co.com.elpoli.service.PreguntaService;
import co.com.elpoli.repository.search.PreguntaSearchRepository;
import co.com.elpoli.service.dto.PreguntaDTO;
import co.com.elpoli.service.mapper.PreguntaMapper;
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
 * Test class for the PreguntaResource REST controller.
 *
 * @see PreguntaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class PreguntaResourceIntTest {

    private static final String DEFAULT_CORTA_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_CORTA_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private PreguntaMapper preguntaMapper;

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private PreguntaSearchRepository preguntaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPreguntaMockMvc;

    private Pregunta pregunta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreguntaResource preguntaResource = new PreguntaResource(preguntaService);
        this.restPreguntaMockMvc = MockMvcBuilders.standaloneSetup(preguntaResource)
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
    public static Pregunta createEntity(EntityManager em) {
        Pregunta pregunta = new Pregunta()
            .cortaDescripcion(DEFAULT_CORTA_DESCRIPCION)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Respuesta respuesta = RespuestaResourceIntTest.createEntity(em);
        em.persist(respuesta);
        em.flush();
        pregunta.getRespuestas().add(respuesta);
        // Add required entity
        Pista pista = PistaResourceIntTest.createEntity(em);
        em.persist(pista);
        em.flush();
        pregunta.getPistas().add(pista);
        // Add required entity
        Cuestionario cuestionario = CuestionarioResourceIntTest.createEntity(em);
        em.persist(cuestionario);
        em.flush();
        pregunta.setCuestionario(cuestionario);
        // Add required entity
        TipoPregunta tipoPregunta = TipoPreguntaResourceIntTest.createEntity(em);
        em.persist(tipoPregunta);
        em.flush();
        pregunta.setTipoPregunta(tipoPregunta);
        return pregunta;
    }

    @Before
    public void initTest() {
        preguntaSearchRepository.deleteAll();
        pregunta = createEntity(em);
    }

    @Test
    @Transactional
    public void createPregunta() throws Exception {
        int databaseSizeBeforeCreate = preguntaRepository.findAll().size();

        // Create the Pregunta
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(pregunta);
        restPreguntaMockMvc.perform(post("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeCreate + 1);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getCortaDescripcion()).isEqualTo(DEFAULT_CORTA_DESCRIPCION);
        assertThat(testPregunta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Pregunta in Elasticsearch
        Pregunta preguntaEs = preguntaSearchRepository.findOne(testPregunta.getId());
        assertThat(preguntaEs).isEqualToComparingFieldByField(testPregunta);
    }

    @Test
    @Transactional
    public void createPreguntaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preguntaRepository.findAll().size();

        // Create the Pregunta with an existing ID
        pregunta.setId(1L);
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(pregunta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreguntaMockMvc.perform(post("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCortaDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = preguntaRepository.findAll().size();
        // set the field null
        pregunta.setCortaDescripcion(null);

        // Create the Pregunta, which fails.
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(pregunta);

        restPreguntaMockMvc.perform(post("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isBadRequest());

        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = preguntaRepository.findAll().size();
        // set the field null
        pregunta.setDescripcion(null);

        // Create the Pregunta, which fails.
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(pregunta);

        restPreguntaMockMvc.perform(post("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isBadRequest());

        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPreguntas() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList
        restPreguntaMockMvc.perform(get("/api/preguntas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getPregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get the pregunta
        restPreguntaMockMvc.perform(get("/api/preguntas/{id}", pregunta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pregunta.getId().intValue()))
            .andExpect(jsonPath("$.cortaDescripcion").value(DEFAULT_CORTA_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPregunta() throws Exception {
        // Get the pregunta
        restPreguntaMockMvc.perform(get("/api/preguntas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);
        preguntaSearchRepository.save(pregunta);
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();

        // Update the pregunta
        Pregunta updatedPregunta = preguntaRepository.findOne(pregunta.getId());
        updatedPregunta
            .cortaDescripcion(UPDATED_CORTA_DESCRIPCION)
            .descripcion(UPDATED_DESCRIPCION);
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(updatedPregunta);

        restPreguntaMockMvc.perform(put("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isOk());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getCortaDescripcion()).isEqualTo(UPDATED_CORTA_DESCRIPCION);
        assertThat(testPregunta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Pregunta in Elasticsearch
        Pregunta preguntaEs = preguntaSearchRepository.findOne(testPregunta.getId());
        assertThat(preguntaEs).isEqualToComparingFieldByField(testPregunta);
    }

    @Test
    @Transactional
    public void updateNonExistingPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();

        // Create the Pregunta
        PreguntaDTO preguntaDTO = preguntaMapper.toDto(pregunta);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPreguntaMockMvc.perform(put("/api/preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preguntaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);
        preguntaSearchRepository.save(pregunta);
        int databaseSizeBeforeDelete = preguntaRepository.findAll().size();

        // Get the pregunta
        restPreguntaMockMvc.perform(delete("/api/preguntas/{id}", pregunta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean preguntaExistsInEs = preguntaSearchRepository.exists(pregunta.getId());
        assertThat(preguntaExistsInEs).isFalse();

        // Validate the database is empty
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);
        preguntaSearchRepository.save(pregunta);

        // Search the pregunta
        restPreguntaMockMvc.perform(get("/api/_search/preguntas?query=id:" + pregunta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pregunta.class);
        Pregunta pregunta1 = new Pregunta();
        pregunta1.setId(1L);
        Pregunta pregunta2 = new Pregunta();
        pregunta2.setId(pregunta1.getId());
        assertThat(pregunta1).isEqualTo(pregunta2);
        pregunta2.setId(2L);
        assertThat(pregunta1).isNotEqualTo(pregunta2);
        pregunta1.setId(null);
        assertThat(pregunta1).isNotEqualTo(pregunta2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreguntaDTO.class);
        PreguntaDTO preguntaDTO1 = new PreguntaDTO();
        preguntaDTO1.setId(1L);
        PreguntaDTO preguntaDTO2 = new PreguntaDTO();
        assertThat(preguntaDTO1).isNotEqualTo(preguntaDTO2);
        preguntaDTO2.setId(preguntaDTO1.getId());
        assertThat(preguntaDTO1).isEqualTo(preguntaDTO2);
        preguntaDTO2.setId(2L);
        assertThat(preguntaDTO1).isNotEqualTo(preguntaDTO2);
        preguntaDTO1.setId(null);
        assertThat(preguntaDTO1).isNotEqualTo(preguntaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preguntaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preguntaMapper.fromId(null)).isNull();
    }
}
