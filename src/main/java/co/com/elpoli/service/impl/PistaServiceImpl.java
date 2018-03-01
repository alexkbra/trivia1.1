package co.com.elpoli.service.impl;

import co.com.elpoli.service.PistaService;
import co.com.elpoli.domain.Pista;
import co.com.elpoli.repository.PistaRepository;
import co.com.elpoli.repository.search.PistaSearchRepository;
import co.com.elpoli.service.dto.PistaDTO;
import co.com.elpoli.service.mapper.PistaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Pista.
 */
@Service
@Transactional
public class PistaServiceImpl implements PistaService{

    private final Logger log = LoggerFactory.getLogger(PistaServiceImpl.class);

    private final PistaRepository pistaRepository;

    private final PistaMapper pistaMapper;

    private final PistaSearchRepository pistaSearchRepository;

    public PistaServiceImpl(PistaRepository pistaRepository, PistaMapper pistaMapper, PistaSearchRepository pistaSearchRepository) {
        this.pistaRepository = pistaRepository;
        this.pistaMapper = pistaMapper;
        this.pistaSearchRepository = pistaSearchRepository;
    }

    /**
     * Save a pista.
     *
     * @param pistaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PistaDTO save(PistaDTO pistaDTO) {
        log.debug("Request to save Pista : {}", pistaDTO);
        Pista pista = pistaMapper.toEntity(pistaDTO);
        pista = pistaRepository.save(pista);
        PistaDTO result = pistaMapper.toDto(pista);
        pistaSearchRepository.save(pista);
        return result;
    }

    /**
     *  Get all the pistas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PistaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pistas");
        return pistaRepository.findAll(pageable)
            .map(pistaMapper::toDto);
    }

    /**
     *  Get one pista by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PistaDTO findOne(Long id) {
        log.debug("Request to get Pista : {}", id);
        Pista pista = pistaRepository.findOne(id);
        return pistaMapper.toDto(pista);
    }

    /**
     *  Delete the  pista by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pista : {}", id);
        pistaRepository.delete(id);
        pistaSearchRepository.delete(id);
    }

    /**
     * Search for the pista corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PistaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pistas for query {}", query);
        Page<Pista> result = pistaSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(pistaMapper::toDto);
    }
}
