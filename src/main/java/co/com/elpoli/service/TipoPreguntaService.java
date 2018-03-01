package co.com.elpoli.service;

import co.com.elpoli.service.dto.TipoPreguntaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TipoPregunta.
 */
public interface TipoPreguntaService {

    /**
     * Save a tipoPregunta.
     *
     * @param tipoPreguntaDTO the entity to save
     * @return the persisted entity
     */
    TipoPreguntaDTO save(TipoPreguntaDTO tipoPreguntaDTO);

    /**
     *  Get all the tipoPreguntas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TipoPreguntaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tipoPregunta.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TipoPreguntaDTO findOne(Long id);

    /**
     *  Delete the "id" tipoPregunta.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the tipoPregunta corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TipoPreguntaDTO> search(String query, Pageable pageable);
}
