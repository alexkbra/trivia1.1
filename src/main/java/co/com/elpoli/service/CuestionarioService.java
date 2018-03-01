package co.com.elpoli.service;

import co.com.elpoli.service.dto.CuestionarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Cuestionario.
 */
public interface CuestionarioService {

    /**
     * Save a cuestionario.
     *
     * @param cuestionarioDTO the entity to save
     * @return the persisted entity
     */
    CuestionarioDTO save(CuestionarioDTO cuestionarioDTO);

    /**
     *  Get all the cuestionarios.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CuestionarioDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" cuestionario.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CuestionarioDTO findOne(Long id);

    /**
     *  Delete the "id" cuestionario.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cuestionario corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CuestionarioDTO> search(String query, Pageable pageable);
}
