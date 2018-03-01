package co.com.elpoli.service.impl;

import co.com.elpoli.service.PublicidadService;
import co.com.elpoli.domain.Publicidad;
import co.com.elpoli.repository.PublicidadRepository;
import co.com.elpoli.repository.search.PublicidadSearchRepository;
import co.com.elpoli.service.dto.PublicidadDTO;
import co.com.elpoli.service.mapper.PublicidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Publicidad.
 */
@Service
@Transactional
public class PublicidadServiceImpl implements PublicidadService{

    private final Logger log = LoggerFactory.getLogger(PublicidadServiceImpl.class);

    private final PublicidadRepository publicidadRepository;

    private final PublicidadMapper publicidadMapper;

    private final PublicidadSearchRepository publicidadSearchRepository;

    public PublicidadServiceImpl(PublicidadRepository publicidadRepository, PublicidadMapper publicidadMapper, PublicidadSearchRepository publicidadSearchRepository) {
        this.publicidadRepository = publicidadRepository;
        this.publicidadMapper = publicidadMapper;
        this.publicidadSearchRepository = publicidadSearchRepository;
    }

    /**
     * Save a publicidad.
     *
     * @param publicidadDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PublicidadDTO save(PublicidadDTO publicidadDTO) {
        log.debug("Request to save Publicidad : {}", publicidadDTO);
        Publicidad publicidad = publicidadMapper.toEntity(publicidadDTO);
        publicidad = publicidadRepository.save(publicidad);
        PublicidadDTO result = publicidadMapper.toDto(publicidad);
        publicidadSearchRepository.save(publicidad);
        return result;
    }

    /**
     *  Get all the publicidads.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PublicidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Publicidads");
        return publicidadRepository.findAll(pageable)
            .map(publicidadMapper::toDto);
    }

    /**
     *  Get one publicidad by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PublicidadDTO findOne(Long id) {
        log.debug("Request to get Publicidad : {}", id);
        Publicidad publicidad = publicidadRepository.findOne(id);
        return publicidadMapper.toDto(publicidad);
    }

    /**
     *  Delete the  publicidad by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Publicidad : {}", id);
        publicidadRepository.delete(id);
        publicidadSearchRepository.delete(id);
    }

    /**
     * Search for the publicidad corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PublicidadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Publicidads for query {}", query);
        Page<Publicidad> result = publicidadSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(publicidadMapper::toDto);
    }
}
