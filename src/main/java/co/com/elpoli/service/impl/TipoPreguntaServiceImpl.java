package co.com.elpoli.service.impl;

import co.com.elpoli.service.TipoPreguntaService;
import co.com.elpoli.domain.TipoPregunta;
import co.com.elpoli.repository.TipoPreguntaRepository;
import co.com.elpoli.repository.search.TipoPreguntaSearchRepository;
import co.com.elpoli.service.dto.TipoPreguntaDTO;
import co.com.elpoli.service.mapper.TipoPreguntaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TipoPregunta.
 */
@Service
@Transactional
public class TipoPreguntaServiceImpl implements TipoPreguntaService{

    private final Logger log = LoggerFactory.getLogger(TipoPreguntaServiceImpl.class);

    private final TipoPreguntaRepository tipoPreguntaRepository;

    private final TipoPreguntaMapper tipoPreguntaMapper;

    private final TipoPreguntaSearchRepository tipoPreguntaSearchRepository;

    public TipoPreguntaServiceImpl(TipoPreguntaRepository tipoPreguntaRepository, TipoPreguntaMapper tipoPreguntaMapper, TipoPreguntaSearchRepository tipoPreguntaSearchRepository) {
        this.tipoPreguntaRepository = tipoPreguntaRepository;
        this.tipoPreguntaMapper = tipoPreguntaMapper;
        this.tipoPreguntaSearchRepository = tipoPreguntaSearchRepository;
    }

    /**
     * Save a tipoPregunta.
     *
     * @param tipoPreguntaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TipoPreguntaDTO save(TipoPreguntaDTO tipoPreguntaDTO) {
        log.debug("Request to save TipoPregunta : {}", tipoPreguntaDTO);
        TipoPregunta tipoPregunta = tipoPreguntaMapper.toEntity(tipoPreguntaDTO);
        tipoPregunta = tipoPreguntaRepository.save(tipoPregunta);
        TipoPreguntaDTO result = tipoPreguntaMapper.toDto(tipoPregunta);
        tipoPreguntaSearchRepository.save(tipoPregunta);
        return result;
    }

    /**
     *  Get all the tipoPreguntas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoPreguntaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoPreguntas");
        return tipoPreguntaRepository.findAll(pageable)
            .map(tipoPreguntaMapper::toDto);
    }

    /**
     *  Get one tipoPregunta by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TipoPreguntaDTO findOne(Long id) {
        log.debug("Request to get TipoPregunta : {}", id);
        TipoPregunta tipoPregunta = tipoPreguntaRepository.findOne(id);
        return tipoPreguntaMapper.toDto(tipoPregunta);
    }

    /**
     *  Delete the  tipoPregunta by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoPregunta : {}", id);
        tipoPreguntaRepository.delete(id);
        tipoPreguntaSearchRepository.delete(id);
    }

    /**
     * Search for the tipoPregunta corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoPreguntaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoPreguntas for query {}", query);
        Page<TipoPregunta> result = tipoPreguntaSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tipoPreguntaMapper::toDto);
    }
}
