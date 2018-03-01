package co.com.elpoli.service;

import co.com.elpoli.service.dto.RespuestaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Respuesta.
 */
public interface RespuestaService {

    /**
     * Save a respuesta.
     *
     * @param respuestaDTO the entity to save
     * @return the persisted entity
     */
    RespuestaDTO save(RespuestaDTO respuestaDTO);

    /**
     *  Get all the respuestas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RespuestaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" respuesta.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RespuestaDTO findOne(Long id);

    /**
     *  Delete the "id" respuesta.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the respuesta corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RespuestaDTO> search(String query, Pageable pageable);
}
