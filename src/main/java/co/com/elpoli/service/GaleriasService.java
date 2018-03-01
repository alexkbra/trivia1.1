package co.com.elpoli.service;

import co.com.elpoli.service.dto.GaleriasDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Galerias.
 */
public interface GaleriasService {

    /**
     * Save a galerias.
     *
     * @param galeriasDTO the entity to save
     * @return the persisted entity
     */
    GaleriasDTO save(GaleriasDTO galeriasDTO);

    /**
     *  Get all the galerias.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GaleriasDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" galerias.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GaleriasDTO findOne(Long id);

    /**
     *  Delete the "id" galerias.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the galerias corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GaleriasDTO> search(String query, Pageable pageable);
}
