package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Respuesta;
import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.repository.RespuestaRepository;
import co.com.elpoli.service.RespuestaService;
import co.com.elpoli.repository.search.RespuestaSearchRepository;
import co.com.elpoli.service.dto.RespuestaDTO;
import co.com.elpoli.service.mapper.RespuestaMapper;
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
 * Test class for the RespuestaResource REST controller.
 *
 * @see RespuestaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class RespuestaResourceIntTest {

    private static final String DEFAULT_CORTA_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_CORTA_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private RespuestaMapper respuestaMapper;

    @Autowired
    private RespuestaService respuestaService;

    @Autowired
    private RespuestaSearchRepository respuestaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRespuestaMockMvc;

    private Respuesta respuesta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RespuestaResource respuestaResource = new RespuestaResource(respuestaService);
        this.restRespuestaMockMvc = MockMvcBuilders.standaloneSetup(respuestaResource)
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
    public static Respuesta createEntity(EntityManager em) {
        Respuesta respuesta = new Respuesta()
            .cortaDescripcion(DEFAULT_CORTA_DESCRIPCION)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Pregunta pregunta = PreguntaResourceIntTest.createEntity(em);
        em.persist(pregunta);
        em.flush();
        respuesta.setPregunta(pregunta);
        return respuesta;
    }

    @Before
    public void initTest() {
        respuestaSearchRepository.deleteAll();
        respuesta = createEntity(em);
    }

    @Test
    @Transactional
    public void createRespuesta() throws Exception {
        int databaseSizeBeforeCreate = respuestaRepository.findAll().size();

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);
        restRespuestaMockMvc.perform(post("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isCreated());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeCreate + 1);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getCortaDescripcion()).isEqualTo(DEFAULT_CORTA_DESCRIPCION);
        assertThat(testRespuesta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Respuesta in Elasticsearch
        Respuesta respuestaEs = respuestaSearchRepository.findOne(testRespuesta.getId());
        assertThat(respuestaEs).isEqualToComparingFieldByField(testRespuesta);
    }

    @Test
    @Transactional
    public void createRespuestaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = respuestaRepository.findAll().size();

        // Create the Respuesta with an existing ID
        respuesta.setId(1L);
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespuestaMockMvc.perform(post("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCortaDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = respuestaRepository.findAll().size();
        // set the field null
        respuesta.setCortaDescripcion(null);

        // Create the Respuesta, which fails.
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        restRespuestaMockMvc.perform(post("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isBadRequest());

        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = respuestaRepository.findAll().size();
        // set the field null
        respuesta.setDescripcion(null);

        // Create the Respuesta, which fails.
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        restRespuestaMockMvc.perform(post("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isBadRequest());

        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRespuestas() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        // Get all the respuestaList
        restRespuestaMockMvc.perform(get("/api/respuestas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);

        // Get the respuesta
        restRespuestaMockMvc.perform(get("/api/respuestas/{id}", respuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(respuesta.getId().intValue()))
            .andExpect(jsonPath("$.cortaDescripcion").value(DEFAULT_CORTA_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRespuesta() throws Exception {
        // Get the respuesta
        restRespuestaMockMvc.perform(get("/api/respuestas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);
        respuestaSearchRepository.save(respuesta);
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();

        // Update the respuesta
        Respuesta updatedRespuesta = respuestaRepository.findOne(respuesta.getId());
        updatedRespuesta
            .cortaDescripcion(UPDATED_CORTA_DESCRIPCION)
            .descripcion(UPDATED_DESCRIPCION);
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(updatedRespuesta);

        restRespuestaMockMvc.perform(put("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isOk());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate);
        Respuesta testRespuesta = respuestaList.get(respuestaList.size() - 1);
        assertThat(testRespuesta.getCortaDescripcion()).isEqualTo(UPDATED_CORTA_DESCRIPCION);
        assertThat(testRespuesta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Respuesta in Elasticsearch
        Respuesta respuestaEs = respuestaSearchRepository.findOne(testRespuesta.getId());
        assertThat(respuestaEs).isEqualToComparingFieldByField(testRespuesta);
    }

    @Test
    @Transactional
    public void updateNonExistingRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = respuestaRepository.findAll().size();

        // Create the Respuesta
        RespuestaDTO respuestaDTO = respuestaMapper.toDto(respuesta);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRespuestaMockMvc.perform(put("/api/respuestas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respuestaDTO)))
            .andExpect(status().isCreated());

        // Validate the Respuesta in the database
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);
        respuestaSearchRepository.save(respuesta);
        int databaseSizeBeforeDelete = respuestaRepository.findAll().size();

        // Get the respuesta
        restRespuestaMockMvc.perform(delete("/api/respuestas/{id}", respuesta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean respuestaExistsInEs = respuestaSearchRepository.exists(respuesta.getId());
        assertThat(respuestaExistsInEs).isFalse();

        // Validate the database is empty
        List<Respuesta> respuestaList = respuestaRepository.findAll();
        assertThat(respuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRespuesta() throws Exception {
        // Initialize the database
        respuestaRepository.saveAndFlush(respuesta);
        respuestaSearchRepository.save(respuesta);

        // Search the respuesta
        restRespuestaMockMvc.perform(get("/api/_search/respuestas?query=id:" + respuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Respuesta.class);
        Respuesta respuesta1 = new Respuesta();
        respuesta1.setId(1L);
        Respuesta respuesta2 = new Respuesta();
        respuesta2.setId(respuesta1.getId());
        assertThat(respuesta1).isEqualTo(respuesta2);
        respuesta2.setId(2L);
        assertThat(respuesta1).isNotEqualTo(respuesta2);
        respuesta1.setId(null);
        assertThat(respuesta1).isNotEqualTo(respuesta2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespuestaDTO.class);
        RespuestaDTO respuestaDTO1 = new RespuestaDTO();
        respuestaDTO1.setId(1L);
        RespuestaDTO respuestaDTO2 = new RespuestaDTO();
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
        respuestaDTO2.setId(respuestaDTO1.getId());
        assertThat(respuestaDTO1).isEqualTo(respuestaDTO2);
        respuestaDTO2.setId(2L);
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
        respuestaDTO1.setId(null);
        assertThat(respuestaDTO1).isNotEqualTo(respuestaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(respuestaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(respuestaMapper.fromId(null)).isNull();
    }
}
