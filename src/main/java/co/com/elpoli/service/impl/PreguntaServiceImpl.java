package co.com.elpoli.service.impl;

import co.com.elpoli.service.PreguntaService;
import co.com.elpoli.domain.Pregunta;
import co.com.elpoli.repository.PreguntaRepository;
import co.com.elpoli.repository.search.PreguntaSearchRepository;
import co.com.elpoli.service.dto.PreguntaDTO;
import co.com.elpoli.service.mapper.PreguntaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Pregunta.
 */
@Service
@Transactional
public class PreguntaServiceImpl implements PreguntaService{

    private final Logger log = LoggerFactory.getLogger(PreguntaServiceImpl.class);

    private final PreguntaRepository preguntaRepository;

    private final PreguntaMapper preguntaMapper;

    private final PreguntaSearchRepository preguntaSearchRepository;

    public PreguntaServiceImpl(PreguntaRepository preguntaRepository, PreguntaMapper preguntaMapper, PreguntaSearchRepository preguntaSearchRepository) {
        this.preguntaRepository = preguntaRepository;
        this.preguntaMapper = preguntaMapper;
        this.preguntaSearchRepository = preguntaSearchRepository;
    }

    /**
     * Save a pregunta.
     *
     * @param preguntaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PreguntaDTO save(PreguntaDTO preguntaDTO) {
        log.debug("Request to save Pregunta : {}", preguntaDTO);
        Pregunta pregunta = preguntaMapper.toEntity(preguntaDTO);
        pregunta = preguntaRepository.save(pregunta);
        PreguntaDTO result = preguntaMapper.toDto(pregunta);
        preguntaSearchRepository.save(pregunta);
        return result;
    }

    /**
     *  Get all the preguntas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreguntaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Preguntas");
        return preguntaRepository.findAll(pageable)
            .map(preguntaMapper::toDto);
    }

    /**
     *  Get one pregunta by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PreguntaDTO findOne(Long id) {
        log.debug("Request to get Pregunta : {}", id);
        Pregunta pregunta = preguntaRepository.findOne(id);
        return preguntaMapper.toDto(pregunta);
    }

    /**
     *  Delete the  pregunta by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pregunta : {}", id);
        preguntaRepository.delete(id);
        preguntaSearchRepository.delete(id);
    }

    /**
     * Search for the pregunta corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreguntaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Preguntas for query {}", query);
        Page<Pregunta> result = preguntaSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(preguntaMapper::toDto);
    }
}
