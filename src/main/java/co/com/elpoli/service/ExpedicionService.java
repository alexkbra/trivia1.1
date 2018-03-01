package co.com.elpoli.service;

import co.com.elpoli.service.dto.ExpedicionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Expedicion.
 */
public interface ExpedicionService {

    /**
     * Save a expedicion.
     *
     * @param expedicionDTO the entity to save
     * @return the persisted entity
     */
    ExpedicionDTO save(ExpedicionDTO expedicionDTO);

    /**
     *  Get all the expedicions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExpedicionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" expedicion.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExpedicionDTO findOne(Long id);

    /**
     *  Delete the "id" expedicion.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the expedicion corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExpedicionDTO> search(String query, Pageable pageable);
}
