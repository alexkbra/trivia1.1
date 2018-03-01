package co.com.elpoli.service;

import co.com.elpoli.service.dto.PistaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pista.
 */
public interface PistaService {

    /**
     * Save a pista.
     *
     * @param pistaDTO the entity to save
     * @return the persisted entity
     */
    PistaDTO save(PistaDTO pistaDTO);

    /**
     *  Get all the pistas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PistaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" pista.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PistaDTO findOne(Long id);

    /**
     *  Delete the "id" pista.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the pista corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PistaDTO> search(String query, Pageable pageable);
}
