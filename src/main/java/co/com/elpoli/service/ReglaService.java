package co.com.elpoli.service;

import co.com.elpoli.service.dto.ReglaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Regla.
 */
public interface ReglaService {

    /**
     * Save a regla.
     *
     * @param reglaDTO the entity to save
     * @return the persisted entity
     */
    ReglaDTO save(ReglaDTO reglaDTO);

    /**
     *  Get all the reglas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReglaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" regla.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ReglaDTO findOne(Long id);

    /**
     *  Delete the "id" regla.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the regla corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ReglaDTO> search(String query, Pageable pageable);
}
