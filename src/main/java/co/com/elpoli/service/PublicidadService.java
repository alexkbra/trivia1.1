package co.com.elpoli.service;

import co.com.elpoli.service.dto.PublicidadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Publicidad.
 */
public interface PublicidadService {

    /**
     * Save a publicidad.
     *
     * @param publicidadDTO the entity to save
     * @return the persisted entity
     */
    PublicidadDTO save(PublicidadDTO publicidadDTO);

    /**
     *  Get all the publicidads.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PublicidadDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" publicidad.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PublicidadDTO findOne(Long id);

    /**
     *  Delete the "id" publicidad.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the publicidad corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PublicidadDTO> search(String query, Pageable pageable);
}
