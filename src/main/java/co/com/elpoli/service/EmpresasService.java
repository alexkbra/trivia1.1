package co.com.elpoli.service;

import co.com.elpoli.service.dto.EmpresasDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Empresas.
 */
public interface EmpresasService {

    /**
     * Save a empresas.
     *
     * @param empresasDTO the entity to save
     * @return the persisted entity
     */
    EmpresasDTO save(EmpresasDTO empresasDTO);

    /**
     *  Get all the empresas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EmpresasDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" empresas.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EmpresasDTO findOne(Long id);

    /**
     *  Delete the "id" empresas.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the empresas corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EmpresasDTO> search(String query, Pageable pageable);
}
