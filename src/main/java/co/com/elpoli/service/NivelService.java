package co.com.elpoli.service;

import co.com.elpoli.service.dto.NivelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Nivel.
 */
public interface NivelService {

    /**
     * Save a nivel.
     *
     * @param nivelDTO the entity to save
     * @return the persisted entity
     */
    NivelDTO save(NivelDTO nivelDTO);

    /**
     *  Get all the nivels.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NivelDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" nivel.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NivelDTO findOne(Long id);

    /**
     *  Delete the "id" nivel.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the nivel corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NivelDTO> search(String query, Pageable pageable);
}
