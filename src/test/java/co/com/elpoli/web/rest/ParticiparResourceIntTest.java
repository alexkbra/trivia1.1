package co.com.elpoli.web.rest;

import co.com.elpoli.TriviaApp;

import co.com.elpoli.domain.Participar;
import co.com.elpoli.repository.ParticiparRepository;
import co.com.elpoli.service.ParticiparService;
import co.com.elpoli.repository.search.ParticiparSearchRepository;
import co.com.elpoli.service.dto.ParticiparDTO;
import co.com.elpoli.service.mapper.ParticiparMapper;
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
 * Test class for the ParticiparResource REST controller.
 *
 * @see ParticiparResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class ParticiparResourceIntTest {

    @Autowired
    private ParticiparRepository participarRepository;

    @Autowired
    private ParticiparMapper participarMapper;

    @Autowired
    private ParticiparService participarService;

    @Autowired
    private ParticiparSearchRepository participarSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParticiparMockMvc;

    private Participar participar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticiparResource participarResource = new ParticiparResource(participarService);
        this.restParticiparMockMvc = MockMvcBuilders.standaloneSetup(participarResource)
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
    public static Participar createEntity(EntityManager em) {
        Participar participar = new Participar();
        return participar;
    }

    @Before
    public void initTest() {
        participarSearchRepository.deleteAll();
        participar = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipar() throws Exception {
        int databaseSizeBeforeCreate = participarRepository.findAll().size();

        // Create the Participar
        ParticiparDTO participarDTO = participarMapper.toDto(participar);
        restParticiparMockMvc.perform(post("/api/participars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participarDTO)))
            .andExpect(status().isCreated());

        // Validate the Participar in the database
        List<Participar> participarList = participarRepository.findAll();
        assertThat(participarList).hasSize(databaseSizeBeforeCreate + 1);
        Participar testParticipar = participarList.get(participarList.size() - 1);

        // Validate the Participar in Elasticsearch
        Participar participarEs = participarSearchRepository.findOne(testParticipar.getId());
        assertThat(participarEs).isEqualToComparingFieldByField(testParticipar);
    }

    @Test
    @Transactional
    public void createParticiparWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participarRepository.findAll().size();

        // Create the Participar with an existing ID
        participar.setId(1L);
        ParticiparDTO participarDTO = participarMapper.toDto(participar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticiparMockMvc.perform(post("/api/participars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participar in the database
        List<Participar> participarList = participarRepository.findAll();
        assertThat(participarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParticipars() throws Exception {
        // Initialize the database
        participarRepository.saveAndFlush(participar);

        // Get all the participarList
        restParticiparMockMvc.perform(get("/api/participars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participar.getId().intValue())));
    }

    @Test
    @Transactional
    public void getParticipar() throws Exception {
        // Initialize the database
        participarRepository.saveAndFlush(participar);

        // Get the participar
        restParticiparMockMvc.perform(get("/api/participars/{id}", participar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participar.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipar() throws Exception {
        // Get the participar
        restParticiparMockMvc.perform(get("/api/participars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipar() throws Exception {
        // Initialize the database
        participarRepository.saveAndFlush(participar);
        participarSearchRepository.save(participar);
        int databaseSizeBeforeUpdate = participarRepository.findAll().size();

        // Update the participar
        Participar updatedParticipar = participarRepository.findOne(participar.getId());
        ParticiparDTO participarDTO = participarMapper.toDto(updatedParticipar);

        restParticiparMockMvc.perform(put("/api/participars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participarDTO)))
            .andExpect(status().isOk());

        // Validate the Participar in the database
        List<Participar> participarList = participarRepository.findAll();
        assertThat(participarList).hasSize(databaseSizeBeforeUpdate);
        Participar testParticipar = participarList.get(participarList.size() - 1);

        // Validate the Participar in Elasticsearch
        Participar participarEs = participarSearchRepository.findOne(testParticipar.getId());
        assertThat(participarEs).isEqualToComparingFieldByField(testParticipar);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipar() throws Exception {
        int databaseSizeBeforeUpdate = participarRepository.findAll().size();

        // Create the Participar
        ParticiparDTO participarDTO = participarMapper.toDto(participar);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParticiparMockMvc.perform(put("/api/participars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participarDTO)))
            .andExpect(status().isCreated());

        // Validate the Participar in the database
        List<Participar> participarList = participarRepository.findAll();
        assertThat(participarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParticipar() throws Exception {
        // Initialize the database
        participarRepository.saveAndFlush(participar);
        participarSearchRepository.save(participar);
        int databaseSizeBeforeDelete = participarRepository.findAll().size();

        // Get the participar
        restParticiparMockMvc.perform(delete("/api/participars/{id}", participar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean participarExistsInEs = participarSearchRepository.exists(participar.getId());
        assertThat(participarExistsInEs).isFalse();

        // Validate the database is empty
        List<Participar> participarList = participarRepository.findAll();
        assertThat(participarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParticipar() throws Exception {
        // Initialize the database
        participarRepository.saveAndFlush(participar);
        participarSearchRepository.save(participar);

        // Search the participar
        restParticiparMockMvc.perform(get("/api/_search/participars?query=id:" + participar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participar.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Participar.class);
        Participar participar1 = new Participar();
        participar1.setId(1L);
        Participar participar2 = new Participar();
        participar2.setId(participar1.getId());
        assertThat(participar1).isEqualTo(participar2);
        participar2.setId(2L);
        assertThat(participar1).isNotEqualTo(participar2);
        participar1.setId(null);
        assertThat(participar1).isNotEqualTo(participar2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticiparDTO.class);
        ParticiparDTO participarDTO1 = new ParticiparDTO();
        participarDTO1.setId(1L);
        ParticiparDTO participarDTO2 = new ParticiparDTO();
        assertThat(participarDTO1).isNotEqualTo(participarDTO2);
        participarDTO2.setId(participarDTO1.getId());
        assertThat(participarDTO1).isEqualTo(participarDTO2);
        participarDTO2.setId(2L);
        assertThat(participarDTO1).isNotEqualTo(participarDTO2);
        participarDTO1.setId(null);
        assertThat(participarDTO1).isNotEqualTo(participarDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(participarMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(participarMapper.fromId(null)).isNull();
    }
}
