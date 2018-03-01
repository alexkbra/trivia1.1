package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Galerias;
import co.com.elpoli.domain.Publicidad;
import co.com.elpoli.repository.GaleriasRepository;
import co.com.elpoli.service.GaleriasService;
import co.com.elpoli.repository.search.GaleriasSearchRepository;
import co.com.elpoli.service.dto.GaleriasDTO;
import co.com.elpoli.service.mapper.GaleriasMapper;
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

import co.com.elpoli.domain.enumeration.TipoArchivo;
/**
 * Test class for the GaleriasResource REST controller.
 *
 * @see GaleriasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class GaleriasResourceIntTest {

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final TipoArchivo DEFAULT_TIPO_ARCHIVO = TipoArchivo.VIDEO;
    private static final TipoArchivo UPDATED_TIPO_ARCHIVO = TipoArchivo.MUSIC;

    @Autowired
    private GaleriasRepository galeriasRepository;

    @Autowired
    private GaleriasMapper galeriasMapper;

    @Autowired
    private GaleriasService galeriasService;

    @Autowired
    private GaleriasSearchRepository galeriasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGaleriasMockMvc;

    private Galerias galerias;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GaleriasResource galeriasResource = new GaleriasResource(galeriasService);
        this.restGaleriasMockMvc = MockMvcBuilders.standaloneSetup(galeriasResource)
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
    public static Galerias createEntity(EntityManager em) {
        Galerias galerias = new Galerias()
            .uri(DEFAULT_URI)
            .tipoArchivo(DEFAULT_TIPO_ARCHIVO);
        // Add required entity
        Publicidad publicidad = PublicidadResourceIntTest.createEntity(em);
        em.persist(publicidad);
        em.flush();
        galerias.setPublicidad(publicidad);
        return galerias;
    }

    @Before
    public void initTest() {
        galeriasSearchRepository.deleteAll();
        galerias = createEntity(em);
    }

    @Test
    @Transactional
    public void createGalerias() throws Exception {
        int databaseSizeBeforeCreate = galeriasRepository.findAll().size();

        // Create the Galerias
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(galerias);
        restGaleriasMockMvc.perform(post("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isCreated());

        // Validate the Galerias in the database
        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeCreate + 1);
        Galerias testGalerias = galeriasList.get(galeriasList.size() - 1);
        assertThat(testGalerias.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testGalerias.getTipoArchivo()).isEqualTo(DEFAULT_TIPO_ARCHIVO);

        // Validate the Galerias in Elasticsearch
        Galerias galeriasEs = galeriasSearchRepository.findOne(testGalerias.getId());
        assertThat(galeriasEs).isEqualToComparingFieldByField(testGalerias);
    }

    @Test
    @Transactional
    public void createGaleriasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = galeriasRepository.findAll().size();

        // Create the Galerias with an existing ID
        galerias.setId(1L);
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(galerias);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGaleriasMockMvc.perform(post("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Galerias in the database
        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUriIsRequired() throws Exception {
        int databaseSizeBeforeTest = galeriasRepository.findAll().size();
        // set the field null
        galerias.setUri(null);

        // Create the Galerias, which fails.
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(galerias);

        restGaleriasMockMvc.perform(post("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isBadRequest());

        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoArchivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = galeriasRepository.findAll().size();
        // set the field null
        galerias.setTipoArchivo(null);

        // Create the Galerias, which fails.
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(galerias);

        restGaleriasMockMvc.perform(post("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isBadRequest());

        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGalerias() throws Exception {
        // Initialize the database
        galeriasRepository.saveAndFlush(galerias);

        // Get all the galeriasList
        restGaleriasMockMvc.perform(get("/api/galerias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(galerias.getId().intValue())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI.toString())))
            .andExpect(jsonPath("$.[*].tipoArchivo").value(hasItem(DEFAULT_TIPO_ARCHIVO.toString())));
    }

    @Test
    @Transactional
    public void getGalerias() throws Exception {
        // Initialize the database
        galeriasRepository.saveAndFlush(galerias);

        // Get the galerias
        restGaleriasMockMvc.perform(get("/api/galerias/{id}", galerias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(galerias.getId().intValue()))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI.toString()))
            .andExpect(jsonPath("$.tipoArchivo").value(DEFAULT_TIPO_ARCHIVO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGalerias() throws Exception {
        // Get the galerias
        restGaleriasMockMvc.perform(get("/api/galerias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGalerias() throws Exception {
        // Initialize the database
        galeriasRepository.saveAndFlush(galerias);
        galeriasSearchRepository.save(galerias);
        int databaseSizeBeforeUpdate = galeriasRepository.findAll().size();

        // Update the galerias
        Galerias updatedGalerias = galeriasRepository.findOne(galerias.getId());
        updatedGalerias
            .uri(UPDATED_URI)
            .tipoArchivo(UPDATED_TIPO_ARCHIVO);
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(updatedGalerias);

        restGaleriasMockMvc.perform(put("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isOk());

        // Validate the Galerias in the database
        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeUpdate);
        Galerias testGalerias = galeriasList.get(galeriasList.size() - 1);
        assertThat(testGalerias.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testGalerias.getTipoArchivo()).isEqualTo(UPDATED_TIPO_ARCHIVO);

        // Validate the Galerias in Elasticsearch
        Galerias galeriasEs = galeriasSearchRepository.findOne(testGalerias.getId());
        assertThat(galeriasEs).isEqualToComparingFieldByField(testGalerias);
    }

    @Test
    @Transactional
    public void updateNonExistingGalerias() throws Exception {
        int databaseSizeBeforeUpdate = galeriasRepository.findAll().size();

        // Create the Galerias
        GaleriasDTO galeriasDTO = galeriasMapper.toDto(galerias);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGaleriasMockMvc.perform(put("/api/galerias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galeriasDTO)))
            .andExpect(status().isCreated());

        // Validate the Galerias in the database
        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGalerias() throws Exception {
        // Initialize the database
        galeriasRepository.saveAndFlush(galerias);
        galeriasSearchRepository.save(galerias);
        int databaseSizeBeforeDelete = galeriasRepository.findAll().size();

        // Get the galerias
        restGaleriasMockMvc.perform(delete("/api/galerias/{id}", galerias.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean galeriasExistsInEs = galeriasSearchRepository.exists(galerias.getId());
        assertThat(galeriasExistsInEs).isFalse();

        // Validate the database is empty
        List<Galerias> galeriasList = galeriasRepository.findAll();
        assertThat(galeriasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGalerias() throws Exception {
        // Initialize the database
        galeriasRepository.saveAndFlush(galerias);
        galeriasSearchRepository.save(galerias);

        // Search the galerias
        restGaleriasMockMvc.perform(get("/api/_search/galerias?query=id:" + galerias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(galerias.getId().intValue())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI.toString())))
            .andExpect(jsonPath("$.[*].tipoArchivo").value(hasItem(DEFAULT_TIPO_ARCHIVO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Galerias.class);
        Galerias galerias1 = new Galerias();
        galerias1.setId(1L);
        Galerias galerias2 = new Galerias();
        galerias2.setId(galerias1.getId());
        assertThat(galerias1).isEqualTo(galerias2);
        galerias2.setId(2L);
        assertThat(galerias1).isNotEqualTo(galerias2);
        galerias1.setId(null);
        assertThat(galerias1).isNotEqualTo(galerias2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GaleriasDTO.class);
        GaleriasDTO galeriasDTO1 = new GaleriasDTO();
        galeriasDTO1.setId(1L);
        GaleriasDTO galeriasDTO2 = new GaleriasDTO();
        assertThat(galeriasDTO1).isNotEqualTo(galeriasDTO2);
        galeriasDTO2.setId(galeriasDTO1.getId());
        assertThat(galeriasDTO1).isEqualTo(galeriasDTO2);
        galeriasDTO2.setId(2L);
        assertThat(galeriasDTO1).isNotEqualTo(galeriasDTO2);
        galeriasDTO1.setId(null);
        assertThat(galeriasDTO1).isNotEqualTo(galeriasDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(galeriasMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(galeriasMapper.fromId(null)).isNull();
    }
}
