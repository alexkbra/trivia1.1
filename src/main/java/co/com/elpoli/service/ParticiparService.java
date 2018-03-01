package co.com.elpoli.service;

import co.com.elpoli.service.dto.ParticiparDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Participar.
 */
public interface ParticiparService {

    /**
     * Save a participar.
     *
     * @param participarDTO the entity to save
     * @return the persisted entity
     */
    ParticiparDTO save(ParticiparDTO participarDTO);

    /**
     *  Get all the participars.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ParticiparDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" participar.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ParticiparDTO findOne(Long id);

    /**
     *  Delete the "id" participar.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the participar corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ParticiparDTO> search(String query, Pageable pageable);
}
