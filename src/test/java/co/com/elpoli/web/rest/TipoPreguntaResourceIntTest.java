package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.TipoPregunta;
import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.repository.TipoPreguntaRepository;
import co.com.elpoli.service.TipoPreguntaService;
import co.com.elpoli.repository.search.TipoPreguntaSearchRepository;
import co.com.elpoli.service.dto.TipoPreguntaDTO;
import co.com.elpoli.service.mapper.TipoPreguntaMapper;
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
 * Test class for the TipoPreguntaResource REST controller.
 *
 * @see TipoPreguntaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class TipoPreguntaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CONSTANT = "AAAAAAAAAA";
    private static final String UPDATED_CONSTANT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;

    @Autowired
    private TipoPreguntaMapper tipoPreguntaMapper;

    @Autowired
    private TipoPreguntaService tipoPreguntaService;

    @Autowired
    private TipoPreguntaSearchRepository tipoPreguntaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipoPreguntaMockMvc;

    private TipoPregunta tipoPregunta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoPreguntaResource tipoPreguntaResource = new TipoPreguntaResource(tipoPreguntaService);
        this.restTipoPreguntaMockMvc = MockMvcBuilders.standaloneSetup(tipoPreguntaResource)
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
    public static TipoPregunta createEntity(EntityManager em) {
        TipoPregunta tipoPregunta = new TipoPregunta()
            .nombre(DEFAULT_NOMBRE)
            .constant(DEFAULT_CONSTANT)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Pregunta pregunta = PreguntaResourceIntTest.createEntity(em);
        em.persist(pregunta);
        em.flush();
        tipoPregunta.getPreguntas().add(pregunta);
        return tipoPregunta;
    }

    @Before
    public void initTest() {
        tipoPreguntaSearchRepository.deleteAll();
        tipoPregunta = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoPregunta() throws Exception {
        int databaseSizeBeforeCreate = tipoPreguntaRepository.findAll().size();

        // Create the TipoPregunta
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);
        restTipoPreguntaMockMvc.perform(post("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoPregunta in the database
        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoPregunta testTipoPregunta = tipoPreguntaList.get(tipoPreguntaList.size() - 1);
        assertThat(testTipoPregunta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoPregunta.getConstant()).isEqualTo(DEFAULT_CONSTANT);
        assertThat(testTipoPregunta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the TipoPregunta in Elasticsearch
        TipoPregunta tipoPreguntaEs = tipoPreguntaSearchRepository.findOne(testTipoPregunta.getId());
        assertThat(tipoPreguntaEs).isEqualToComparingFieldByField(testTipoPregunta);
    }

    @Test
    @Transactional
    public void createTipoPreguntaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoPreguntaRepository.findAll().size();

        // Create the TipoPregunta with an existing ID
        tipoPregunta.setId(1L);
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoPreguntaMockMvc.perform(post("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoPregunta in the database
        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoPreguntaRepository.findAll().size();
        // set the field null
        tipoPregunta.setNombre(null);

        // Create the TipoPregunta, which fails.
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);

        restTipoPreguntaMockMvc.perform(post("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoPreguntaRepository.findAll().size();
        // set the field null
        tipoPregunta.setConstant(null);

        // Create the TipoPregunta, which fails.
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);

        restTipoPreguntaMockMvc.perform(post("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoPreguntaRepository.findAll().size();
        // set the field null
        tipoPregunta.setDescripcion(null);

        // Create the TipoPregunta, which fails.
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);

        restTipoPreguntaMockMvc.perform(post("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoPreguntas() throws Exception {
        // Initialize the database
        tipoPreguntaRepository.saveAndFlush(tipoPregunta);

        // Get all the tipoPreguntaList
        restTipoPreguntaMockMvc.perform(get("/api/tipo-preguntas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoPregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].constant").value(hasItem(DEFAULT_CONSTANT.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getTipoPregunta() throws Exception {
        // Initialize the database
        tipoPreguntaRepository.saveAndFlush(tipoPregunta);

        // Get the tipoPregunta
        restTipoPreguntaMockMvc.perform(get("/api/tipo-preguntas/{id}", tipoPregunta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoPregunta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.constant").value(DEFAULT_CONSTANT.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoPregunta() throws Exception {
        // Get the tipoPregunta
        restTipoPreguntaMockMvc.perform(get("/api/tipo-preguntas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoPregunta() throws Exception {
        // Initialize the database
        tipoPreguntaRepository.saveAndFlush(tipoPregunta);
        tipoPreguntaSearchRepository.save(tipoPregunta);
        int databaseSizeBeforeUpdate = tipoPreguntaRepository.findAll().size();

        // Update the tipoPregunta
        TipoPregunta updatedTipoPregunta = tipoPreguntaRepository.findOne(tipoPregunta.getId());
        updatedTipoPregunta
            .nombre(UPDATED_NOMBRE)
            .constant(UPDATED_CONSTANT)
            .descripcion(UPDATED_DESCRIPCION);
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(updatedTipoPregunta);

        restTipoPreguntaMockMvc.perform(put("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isOk());

        // Validate the TipoPregunta in the database
        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeUpdate);
        TipoPregunta testTipoPregunta = tipoPreguntaList.get(tipoPreguntaList.size() - 1);
        assertThat(testTipoPregunta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoPregunta.getConstant()).isEqualTo(UPDATED_CONSTANT);
        assertThat(testTipoPregunta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the TipoPregunta in Elasticsearch
        TipoPregunta tipoPreguntaEs = tipoPreguntaSearchRepository.findOne(testTipoPregunta.getId());
        assertThat(tipoPreguntaEs).isEqualToComparingFieldByField(testTipoPregunta);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoPregunta() throws Exception {
        int databaseSizeBeforeUpdate = tipoPreguntaRepository.findAll().size();

        // Create the TipoPregunta
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaMapper.toDto(tipoPregunta);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipoPreguntaMockMvc.perform(put("/api/tipo-preguntas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPreguntaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoPregunta in the database
        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipoPregunta() throws Exception {
        // Initialize the database
        tipoPreguntaRepository.saveAndFlush(tipoPregunta);
        tipoPreguntaSearchRepository.save(tipoPregunta);
        int databaseSizeBeforeDelete = tipoPreguntaRepository.findAll().size();

        // Get the tipoPregunta
        restTipoPreguntaMockMvc.perform(delete("/api/tipo-preguntas/{id}", tipoPregunta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipoPreguntaExistsInEs = tipoPreguntaSearchRepository.exists(tipoPregunta.getId());
        assertThat(tipoPreguntaExistsInEs).isFalse();

        // Validate the database is empty
        List<TipoPregunta> tipoPreguntaList = tipoPreguntaRepository.findAll();
        assertThat(tipoPreguntaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipoPregunta() throws Exception {
        // Initialize the database
        tipoPreguntaRepository.saveAndFlush(tipoPregunta);
        tipoPreguntaSearchRepository.save(tipoPregunta);

        // Search the tipoPregunta
        restTipoPreguntaMockMvc.perform(get("/api/_search/tipo-preguntas?query=id:" + tipoPregunta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoPregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].constant").value(hasItem(DEFAULT_CONSTANT.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoPregunta.class);
        TipoPregunta tipoPregunta1 = new TipoPregunta();
        tipoPregunta1.setId(1L);
        TipoPregunta tipoPregunta2 = new TipoPregunta();
        tipoPregunta2.setId(tipoPregunta1.getId());
        assertThat(tipoPregunta1).isEqualTo(tipoPregunta2);
        tipoPregunta2.setId(2L);
        assertThat(tipoPregunta1).isNotEqualTo(tipoPregunta2);
        tipoPregunta1.setId(null);
        assertThat(tipoPregunta1).isNotEqualTo(tipoPregunta2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoPreguntaDTO.class);
        TipoPreguntaDTO tipoPreguntaDTO1 = new TipoPreguntaDTO();
        tipoPreguntaDTO1.setId(1L);
        TipoPreguntaDTO tipoPreguntaDTO2 = new TipoPreguntaDTO();
        assertThat(tipoPreguntaDTO1).isNotEqualTo(tipoPreguntaDTO2);
        tipoPreguntaDTO2.setId(tipoPreguntaDTO1.getId());
        assertThat(tipoPreguntaDTO1).isEqualTo(tipoPreguntaDTO2);
        tipoPreguntaDTO2.setId(2L);
        assertThat(tipoPreguntaDTO1).isNotEqualTo(tipoPreguntaDTO2);
        tipoPreguntaDTO1.setId(null);
        assertThat(tipoPreguntaDTO1).isNotEqualTo(tipoPreguntaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoPreguntaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoPreguntaMapper.fromId(null)).isNull();
    }
}
