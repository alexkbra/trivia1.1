package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Pista;
import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.repository.PistaRepository;
import co.com.elpoli.service.PistaService;
import co.com.elpoli.repository.search.PistaSearchRepository;
import co.com.elpoli.service.dto.PistaDTO;
import co.com.elpoli.service.mapper.PistaMapper;
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
 * Test class for the PistaResource REST controller.
 *
 * @see PistaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class PistaResourceIntTest {

    private static final String DEFAULT_CORTA_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_CORTA_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private PistaRepository pistaRepository;

    @Autowired
    private PistaMapper pistaMapper;

    @Autowired
    private PistaService pistaService;

    @Autowired
    private PistaSearchRepository pistaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPistaMockMvc;

    private Pista pista;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PistaResource pistaResource = new PistaResource(pistaService);
        this.restPistaMockMvc = MockMvcBuilders.standaloneSetup(pistaResource)
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
    public static Pista createEntity(EntityManager em) {
        Pista pista = new Pista()
            .cortaDescripcion(DEFAULT_CORTA_DESCRIPCION)
            .descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Pregunta pregunta = PreguntaResourceIntTest.createEntity(em);
        em.persist(pregunta);
        em.flush();
        pista.setPregunta(pregunta);
        return pista;
    }

    @Before
    public void initTest() {
        pistaSearchRepository.deleteAll();
        pista = createEntity(em);
    }

    @Test
    @Transactional
    public void createPista() throws Exception {
        int databaseSizeBeforeCreate = pistaRepository.findAll().size();

        // Create the Pista
        PistaDTO pistaDTO = pistaMapper.toDto(pista);
        restPistaMockMvc.perform(post("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pista in the database
        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeCreate + 1);
        Pista testPista = pistaList.get(pistaList.size() - 1);
        assertThat(testPista.getCortaDescripcion()).isEqualTo(DEFAULT_CORTA_DESCRIPCION);
        assertThat(testPista.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Pista in Elasticsearch
        Pista pistaEs = pistaSearchRepository.findOne(testPista.getId());
        assertThat(pistaEs).isEqualToComparingFieldByField(testPista);
    }

    @Test
    @Transactional
    public void createPistaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pistaRepository.findAll().size();

        // Create the Pista with an existing ID
        pista.setId(1L);
        PistaDTO pistaDTO = pistaMapper.toDto(pista);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPistaMockMvc.perform(post("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pista in the database
        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCortaDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pistaRepository.findAll().size();
        // set the field null
        pista.setCortaDescripcion(null);

        // Create the Pista, which fails.
        PistaDTO pistaDTO = pistaMapper.toDto(pista);

        restPistaMockMvc.perform(post("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isBadRequest());

        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pistaRepository.findAll().size();
        // set the field null
        pista.setDescripcion(null);

        // Create the Pista, which fails.
        PistaDTO pistaDTO = pistaMapper.toDto(pista);

        restPistaMockMvc.perform(post("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isBadRequest());

        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPistas() throws Exception {
        // Initialize the database
        pistaRepository.saveAndFlush(pista);

        // Get all the pistaList
        restPistaMockMvc.perform(get("/api/pistas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pista.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getPista() throws Exception {
        // Initialize the database
        pistaRepository.saveAndFlush(pista);

        // Get the pista
        restPistaMockMvc.perform(get("/api/pistas/{id}", pista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pista.getId().intValue()))
            .andExpect(jsonPath("$.cortaDescripcion").value(DEFAULT_CORTA_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPista() throws Exception {
        // Get the pista
        restPistaMockMvc.perform(get("/api/pistas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePista() throws Exception {
        // Initialize the database
        pistaRepository.saveAndFlush(pista);
        pistaSearchRepository.save(pista);
        int databaseSizeBeforeUpdate = pistaRepository.findAll().size();

        // Update the pista
        Pista updatedPista = pistaRepository.findOne(pista.getId());
        updatedPista
            .cortaDescripcion(UPDATED_CORTA_DESCRIPCION)
            .descripcion(UPDATED_DESCRIPCION);
        PistaDTO pistaDTO = pistaMapper.toDto(updatedPista);

        restPistaMockMvc.perform(put("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isOk());

        // Validate the Pista in the database
        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeUpdate);
        Pista testPista = pistaList.get(pistaList.size() - 1);
        assertThat(testPista.getCortaDescripcion()).isEqualTo(UPDATED_CORTA_DESCRIPCION);
        assertThat(testPista.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Pista in Elasticsearch
        Pista pistaEs = pistaSearchRepository.findOne(testPista.getId());
        assertThat(pistaEs).isEqualToComparingFieldByField(testPista);
    }

    @Test
    @Transactional
    public void updateNonExistingPista() throws Exception {
        int databaseSizeBeforeUpdate = pistaRepository.findAll().size();

        // Create the Pista
        PistaDTO pistaDTO = pistaMapper.toDto(pista);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPistaMockMvc.perform(put("/api/pistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pistaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pista in the database
        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePista() throws Exception {
        // Initialize the database
        pistaRepository.saveAndFlush(pista);
        pistaSearchRepository.save(pista);
        int databaseSizeBeforeDelete = pistaRepository.findAll().size();

        // Get the pista
        restPistaMockMvc.perform(delete("/api/pistas/{id}", pista.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pistaExistsInEs = pistaSearchRepository.exists(pista.getId());
        assertThat(pistaExistsInEs).isFalse();

        // Validate the database is empty
        List<Pista> pistaList = pistaRepository.findAll();
        assertThat(pistaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPista() throws Exception {
        // Initialize the database
        pistaRepository.saveAndFlush(pista);
        pistaSearchRepository.save(pista);

        // Search the pista
        restPistaMockMvc.perform(get("/api/_search/pistas?query=id:" + pista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pista.getId().intValue())))
            .andExpect(jsonPath("$.[*].cortaDescripcion").value(hasItem(DEFAULT_CORTA_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pista.class);
        Pista pista1 = new Pista();
        pista1.setId(1L);
        Pista pista2 = new Pista();
        pista2.setId(pista1.getId());
        assertThat(pista1).isEqualTo(pista2);
        pista2.setId(2L);
        assertThat(pista1).isNotEqualTo(pista2);
        pista1.setId(null);
        assertThat(pista1).isNotEqualTo(pista2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PistaDTO.class);
        PistaDTO pistaDTO1 = new PistaDTO();
        pistaDTO1.setId(1L);
        PistaDTO pistaDTO2 = new PistaDTO();
        assertThat(pistaDTO1).isNotEqualTo(pistaDTO2);
        pistaDTO2.setId(pistaDTO1.getId());
        assertThat(pistaDTO1).isEqualTo(pistaDTO2);
        pistaDTO2.setId(2L);
        assertThat(pistaDTO1).isNotEqualTo(pistaDTO2);
        pistaDTO1.setId(null);
        assertThat(pistaDTO1).isNotEqualTo(pistaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pistaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pistaMapper.fromId(null)).isNull();
    }
}
