package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Publicidad;
import co.com.elpoli.domain.Galerias;
import co.com.elpoli.domain.Empresas;
import co.com.elpoli.repository.PublicidadRepository;
import co.com.elpoli.service.PublicidadService;
import co.com.elpoli.repository.search.PublicidadSearchRepository;
import co.com.elpoli.service.dto.PublicidadDTO;
import co.com.elpoli.service.mapper.PublicidadMapper;
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
 * Test class for the PublicidadResource REST controller.
 *
 * @see PublicidadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class PublicidadResourceIntTest {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_PREMIODESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_PREMIODESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private PublicidadRepository publicidadRepository;

    @Autowired
    private PublicidadMapper publicidadMapper;

    @Autowired
    private PublicidadService publicidadService;

    @Autowired
    private PublicidadSearchRepository publicidadSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPublicidadMockMvc;

    private Publicidad publicidad;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PublicidadResource publicidadResource = new PublicidadResource(publicidadService);
        this.restPublicidadMockMvc = MockMvcBuilders.standaloneSetup(publicidadResource)
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
    public static Publicidad createEntity(EntityManager em) {
        Publicidad publicidad = new Publicidad()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .premiodescripcion(DEFAULT_PREMIODESCRIPCION);
        // Add required entity
        Galerias galeria = GaleriasResourceIntTest.createEntity(em);
        em.persist(galeria);
        em.flush();
        publicidad.getGalerias().add(galeria);
        // Add required entity
        Empresas empresas = EmpresasResourceIntTest.createEntity(em);
        em.persist(empresas);
        em.flush();
        publicidad.setEmpresas(empresas);
        return publicidad;
    }

    @Before
    public void initTest() {
        publicidadSearchRepository.deleteAll();
        publicidad = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublicidad() throws Exception {
        int databaseSizeBeforeCreate = publicidadRepository.findAll().size();

        // Create the Publicidad
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);
        restPublicidadMockMvc.perform(post("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isCreated());

        // Validate the Publicidad in the database
        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeCreate + 1);
        Publicidad testPublicidad = publicidadList.get(publicidadList.size() - 1);
        assertThat(testPublicidad.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPublicidad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPublicidad.getPremiodescripcion()).isEqualTo(DEFAULT_PREMIODESCRIPCION);

        // Validate the Publicidad in Elasticsearch
        Publicidad publicidadEs = publicidadSearchRepository.findOne(testPublicidad.getId());
        assertThat(publicidadEs).isEqualToComparingFieldByField(testPublicidad);
    }

    @Test
    @Transactional
    public void createPublicidadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publicidadRepository.findAll().size();

        // Create the Publicidad with an existing ID
        publicidad.setId(1L);
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicidadMockMvc.perform(post("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publicidad in the database
        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicidadRepository.findAll().size();
        // set the field null
        publicidad.setTitulo(null);

        // Create the Publicidad, which fails.
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);

        restPublicidadMockMvc.perform(post("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isBadRequest());

        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicidadRepository.findAll().size();
        // set the field null
        publicidad.setDescripcion(null);

        // Create the Publicidad, which fails.
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);

        restPublicidadMockMvc.perform(post("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isBadRequest());

        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPremiodescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicidadRepository.findAll().size();
        // set the field null
        publicidad.setPremiodescripcion(null);

        // Create the Publicidad, which fails.
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);

        restPublicidadMockMvc.perform(post("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isBadRequest());

        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPublicidads() throws Exception {
        // Initialize the database
        publicidadRepository.saveAndFlush(publicidad);

        // Get all the publicidadList
        restPublicidadMockMvc.perform(get("/api/publicidads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].premiodescripcion").value(hasItem(DEFAULT_PREMIODESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getPublicidad() throws Exception {
        // Initialize the database
        publicidadRepository.saveAndFlush(publicidad);

        // Get the publicidad
        restPublicidadMockMvc.perform(get("/api/publicidads/{id}", publicidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publicidad.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.premiodescripcion").value(DEFAULT_PREMIODESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPublicidad() throws Exception {
        // Get the publicidad
        restPublicidadMockMvc.perform(get("/api/publicidads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublicidad() throws Exception {
        // Initialize the database
        publicidadRepository.saveAndFlush(publicidad);
        publicidadSearchRepository.save(publicidad);
        int databaseSizeBeforeUpdate = publicidadRepository.findAll().size();

        // Update the publicidad
        Publicidad updatedPublicidad = publicidadRepository.findOne(publicidad.getId());
        updatedPublicidad
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .premiodescripcion(UPDATED_PREMIODESCRIPCION);
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(updatedPublicidad);

        restPublicidadMockMvc.perform(put("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isOk());

        // Validate the Publicidad in the database
        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeUpdate);
        Publicidad testPublicidad = publicidadList.get(publicidadList.size() - 1);
        assertThat(testPublicidad.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPublicidad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPublicidad.getPremiodescripcion()).isEqualTo(UPDATED_PREMIODESCRIPCION);

        // Validate the Publicidad in Elasticsearch
        Publicidad publicidadEs = publicidadSearchRepository.findOne(testPublicidad.getId());
        assertThat(publicidadEs).isEqualToComparingFieldByField(testPublicidad);
    }

    @Test
    @Transactional
    public void updateNonExistingPublicidad() throws Exception {
        int databaseSizeBeforeUpdate = publicidadRepository.findAll().size();

        // Create the Publicidad
        PublicidadDTO publicidadDTO = publicidadMapper.toDto(publicidad);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPublicidadMockMvc.perform(put("/api/publicidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicidadDTO)))
            .andExpect(status().isCreated());

        // Validate the Publicidad in the database
        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePublicidad() throws Exception {
        // Initialize the database
        publicidadRepository.saveAndFlush(publicidad);
        publicidadSearchRepository.save(publicidad);
        int databaseSizeBeforeDelete = publicidadRepository.findAll().size();

        // Get the publicidad
        restPublicidadMockMvc.perform(delete("/api/publicidads/{id}", publicidad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean publicidadExistsInEs = publicidadSearchRepository.exists(publicidad.getId());
        assertThat(publicidadExistsInEs).isFalse();

        // Validate the database is empty
        List<Publicidad> publicidadList = publicidadRepository.findAll();
        assertThat(publicidadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPublicidad() throws Exception {
        // Initialize the database
        publicidadRepository.saveAndFlush(publicidad);
        publicidadSearchRepository.save(publicidad);

        // Search the publicidad
        restPublicidadMockMvc.perform(get("/api/_search/publicidads?query=id:" + publicidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].premiodescripcion").value(hasItem(DEFAULT_PREMIODESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publicidad.class);
        Publicidad publicidad1 = new Publicidad();
        publicidad1.setId(1L);
        Publicidad publicidad2 = new Publicidad();
        publicidad2.setId(publicidad1.getId());
        assertThat(publicidad1).isEqualTo(publicidad2);
        publicidad2.setId(2L);
        assertThat(publicidad1).isNotEqualTo(publicidad2);
        publicidad1.setId(null);
        assertThat(publicidad1).isNotEqualTo(publicidad2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicidadDTO.class);
        PublicidadDTO publicidadDTO1 = new PublicidadDTO();
        publicidadDTO1.setId(1L);
        PublicidadDTO publicidadDTO2 = new PublicidadDTO();
        assertThat(publicidadDTO1).isNotEqualTo(publicidadDTO2);
        publicidadDTO2.setId(publicidadDTO1.getId());
        assertThat(publicidadDTO1).isEqualTo(publicidadDTO2);
        publicidadDTO2.setId(2L);
        assertThat(publicidadDTO1).isNotEqualTo(publicidadDTO2);
        publicidadDTO1.setId(null);
        assertThat(publicidadDTO1).isNotEqualTo(publicidadDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(publicidadMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(publicidadMapper.fromId(null)).isNull();
    }
}
