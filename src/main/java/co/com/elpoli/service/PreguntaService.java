package co.com.elpoli.service;

import co.com.elpoli.service.dto.PreguntaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pregunta.
 */
public interface PreguntaService {

    /**
     * Save a pregunta.
     *
     * @param preguntaDTO the entity to save
     * @return the persisted entity
     */
    PreguntaDTO save(PreguntaDTO preguntaDTO);

    /**
     *  Get all the preguntas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PreguntaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" pregunta.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PreguntaDTO findOne(Long id);

    /**
     *  Delete the "id" pregunta.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the pregunta corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PreguntaDTO> search(String query, Pageable pageable);
}
