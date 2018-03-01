package co.com.elpoli.service.impl;

import co.com.elpoli.service.CuestionarioService;
import co.com.elpoli.domain.Cuestionario;
import co.com.elpoli.repository.CuestionarioRepository;
import co.com.elpoli.repository.search.CuestionarioSearchRepository;
import co.com.elpoli.service.dto.CuestionarioDTO;
import co.com.elpoli.service.mapper.CuestionarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Cuestionario.
 */
@Service
@Transactional
public class CuestionarioServiceImpl implements CuestionarioService{

    private final Logger log = LoggerFactory.getLogger(CuestionarioServiceImpl.class);

    private final CuestionarioRepository cuestionarioRepository;

    private final CuestionarioMapper cuestionarioMapper;

    private final CuestionarioSearchRepository cuestionarioSearchRepository;

    public CuestionarioServiceImpl(CuestionarioRepository cuestionarioRepository, CuestionarioMapper cuestionarioMapper, CuestionarioSearchRepository cuestionarioSearchRepository) {
        this.cuestionarioRepository = cuestionarioRepository;
        this.cuestionarioMapper = cuestionarioMapper;
        this.cuestionarioSearchRepository = cuestionarioSearchRepository;
    }

    /**
     * Save a cuestionario.
     *
     * @param cuestionarioDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CuestionarioDTO save(CuestionarioDTO cuestionarioDTO) {
        log.debug("Request to save Cuestionario : {}", cuestionarioDTO);
        Cuestionario cuestionario = cuestionarioMapper.toEntity(cuestionarioDTO);
        cuestionario = cuestionarioRepository.save(cuestionario);
        CuestionarioDTO result = cuestionarioMapper.toDto(cuestionario);
        cuestionarioSearchRepository.save(cuestionario);
        return result;
    }

    /**
     *  Get all the cuestionarios.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CuestionarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cuestionarios");
        return cuestionarioRepository.findAll(pageable)
            .map(cuestionarioMapper::toDto);
    }

    /**
     *  Get one cuestionario by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CuestionarioDTO findOne(Long id) {
        log.debug("Request to get Cuestionario : {}", id);
        Cuestionario cuestionario = cuestionarioRepository.findOneWithEagerRelationships(id);
        return cuestionarioMapper.toDto(cuestionario);
    }

    /**
     *  Delete the  cuestionario by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cuestionario : {}", id);
        cuestionarioRepository.delete(id);
        cuestionarioSearchRepository.delete(id);
    }

    /**
     * Search for the cuestionario corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CuestionarioDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cuestionarios for query {}", query);
        Page<Cuestionario> result = cuestionarioSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(cuestionarioMapper::toDto);
    }
}
