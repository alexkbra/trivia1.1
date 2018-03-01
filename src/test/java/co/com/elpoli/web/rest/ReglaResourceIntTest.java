package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Regla;
import co.com.elpoli.domain.Cuestionario;
import co.com.elpoli.repository.ReglaRepository;
import co.com.elpoli.service.ReglaService;
import co.com.elpoli.repository.search.ReglaSearchRepository;
import co.com.elpoli.service.dto.ReglaDTO;
import co.com.elpoli.service.mapper.ReglaMapper;
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
 * Test class for the ReglaResource REST controller.
 *
 * @see ReglaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class ReglaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private ReglaRepository reglaRepository;

    @Autowired
    private ReglaMapper reglaMapper;

    @Autowired
    private ReglaService reglaService;

    @Autowired
    private ReglaSearchRepository reglaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReglaMockMvc;

    private Regla regla;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReglaResource reglaResource = new ReglaResource(reglaService);
        this.restReglaMockMvc = MockMvcBuilders.standaloneSetup(reglaResource)
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
    public static Regla createEntity(EntityManager em) {
        Regla regla = new Regla()
            .nombre(DEFAULT_NOMBRE);
        // Add required entity
        Cuestionario cuestionario = CuestionarioResourceIntTest.createEntity(em);
        em.persist(cuestionario);
        em.flush();
        regla.setCuestionario(cuestionario);
        return regla;
    }

    @Before
    public void initTest() {
        reglaSearchRepository.deleteAll();
        regla = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegla() throws Exception {
        int databaseSizeBeforeCreate = reglaRepository.findAll().size();

        // Create the Regla
        ReglaDTO reglaDTO = reglaMapper.toDto(regla);
        restReglaMockMvc.perform(post("/api/reglas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglaDTO)))
            .andExpect(status().isCreated());

        // Validate the Regla in the database
        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeCreate + 1);
        Regla testRegla = reglaList.get(reglaList.size() - 1);
        assertThat(testRegla.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the Regla in Elasticsearch
        Regla reglaEs = reglaSearchRepository.findOne(testRegla.getId());
        assertThat(reglaEs).isEqualToComparingFieldByField(testRegla);
    }

    @Test
    @Transactional
    public void createReglaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reglaRepository.findAll().size();

        // Create the Regla with an existing ID
        regla.setId(1L);
        ReglaDTO reglaDTO = reglaMapper.toDto(regla);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReglaMockMvc.perform(post("/api/reglas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regla in the database
        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglaRepository.findAll().size();
        // set the field null
        regla.setNombre(null);

        // Create the Regla, which fails.
        ReglaDTO reglaDTO = reglaMapper.toDto(regla);

        restReglaMockMvc.perform(post("/api/reglas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglaDTO)))
            .andExpect(status().isBadRequest());

        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReglas() throws Exception {
        // Initialize the database
        reglaRepository.saveAndFlush(regla);

        // Get all the reglaList
        restReglaMockMvc.perform(get("/api/reglas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getRegla() throws Exception {
        // Initialize the database
        reglaRepository.saveAndFlush(regla);

        // Get the regla
        restReglaMockMvc.perform(get("/api/reglas/{id}", regla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(regla.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegla() throws Exception {
        // Get the regla
        restReglaMockMvc.perform(get("/api/reglas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegla() throws Exception {
        // Initialize the database
        reglaRepository.saveAndFlush(regla);
        reglaSearchRepository.save(regla);
        int databaseSizeBeforeUpdate = reglaRepository.findAll().size();

        // Update the regla
        Regla updatedRegla = reglaRepository.findOne(regla.getId());
        updatedRegla
            .nombre(UPDATED_NOMBRE);
        ReglaDTO reglaDTO = reglaMapper.toDto(updatedRegla);

        restReglaMockMvc.perform(put("/api/reglas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglaDTO)))
            .andExpect(status().isOk());

        // Validate the Regla in the database
        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeUpdate);
        Regla testRegla = reglaList.get(reglaList.size() - 1);
        assertThat(testRegla.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the Regla in Elasticsearch
        Regla reglaEs = reglaSearchRepository.findOne(testRegla.getId());
        assertThat(reglaEs).isEqualToComparingFieldByField(testRegla);
    }

    @Test
    @Transactional
    public void updateNonExistingRegla() throws Exception {
        int databaseSizeBeforeUpdate = reglaRepository.findAll().size();

        // Create the Regla
        ReglaDTO reglaDTO = reglaMapper.toDto(regla);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReglaMockMvc.perform(put("/api/reglas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reglaDTO)))
            .andExpect(status().isCreated());

        // Validate the Regla in the database
        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegla() throws Exception {
        // Initialize the database
        reglaRepository.saveAndFlush(regla);
        reglaSearchRepository.save(regla);
        int databaseSizeBeforeDelete = reglaRepository.findAll().size();

        // Get the regla
        restReglaMockMvc.perform(delete("/api/reglas/{id}", regla.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reglaExistsInEs = reglaSearchRepository.exists(regla.getId());
        assertThat(reglaExistsInEs).isFalse();

        // Validate the database is empty
        List<Regla> reglaList = reglaRepository.findAll();
        assertThat(reglaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegla() throws Exception {
        // Initialize the database
        reglaRepository.saveAndFlush(regla);
        reglaSearchRepository.save(regla);

        // Search the regla
        restReglaMockMvc.perform(get("/api/_search/reglas?query=id:" + regla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regla.class);
        Regla regla1 = new Regla();
        regla1.setId(1L);
        Regla regla2 = new Regla();
        regla2.setId(regla1.getId());
        assertThat(regla1).isEqualTo(regla2);
        regla2.setId(2L);
        assertThat(regla1).isNotEqualTo(regla2);
        regla1.setId(null);
        assertThat(regla1).isNotEqualTo(regla2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReglaDTO.class);
        ReglaDTO reglaDTO1 = new ReglaDTO();
        reglaDTO1.setId(1L);
        ReglaDTO reglaDTO2 = new ReglaDTO();
        assertThat(reglaDTO1).isNotEqualTo(reglaDTO2);
        reglaDTO2.setId(reglaDTO1.getId());
        assertThat(reglaDTO1).isEqualTo(reglaDTO2);
        reglaDTO2.setId(2L);
        assertThat(reglaDTO1).isNotEqualTo(reglaDTO2);
        reglaDTO1.setId(null);
        assertThat(reglaDTO1).isNotEqualTo(reglaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reglaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reglaMapper.fromId(null)).isNull();
    }
}
