package co.com.elpoli.service.impl;

import co.com.elpoli.service.GaleriasService;
import co.com.elpoli.domain.Galerias;
import co.com.elpoli.repository.GaleriasRepository;
import co.com.elpoli.repository.search.GaleriasSearchRepository;
import co.com.elpoli.service.dto.GaleriasDTO;
import co.com.elpoli.service.mapper.GaleriasMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Galerias.
 */
@Service
@Transactional
public class GaleriasServiceImpl implements GaleriasService{

    private final Logger log = LoggerFactory.getLogger(GaleriasServiceImpl.class);

    private final GaleriasRepository galeriasRepository;

    private final GaleriasMapper galeriasMapper;

    private final GaleriasSearchRepository galeriasSearchRepository;

    public GaleriasServiceImpl(GaleriasRepository galeriasRepository, GaleriasMapper galeriasMapper, GaleriasSearchRepository galeriasSearchRepository) {
        this.galeriasRepository = galeriasRepository;
        this.galeriasMapper = galeriasMapper;
        this.galeriasSearchRepository = galeriasSearchRepository;
    }

    /**
     * Save a galerias.
     *
     * @param galeriasDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GaleriasDTO save(GaleriasDTO galeriasDTO) {
        log.debug("Request to save Galerias : {}", galeriasDTO);
        Galerias galerias = galeriasMapper.toEntity(galeriasDTO);
        galerias = galeriasRepository.save(galerias);
        GaleriasDTO result = galeriasMapper.toDto(galerias);
        galeriasSearchRepository.save(galerias);
        return result;
    }

    /**
     *  Get all the galerias.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GaleriasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Galerias");
        return galeriasRepository.findAll(pageable)
            .map(galeriasMapper::toDto);
    }

    /**
     *  Get one galerias by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GaleriasDTO findOne(Long id) {
        log.debug("Request to get Galerias : {}", id);
        Galerias galerias = galeriasRepository.findOne(id);
        return galeriasMapper.toDto(galerias);
    }

    /**
     *  Delete the  galerias by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Galerias : {}", id);
        galeriasRepository.delete(id);
        galeriasSearchRepository.delete(id);
    }

    /**
     * Search for the galerias corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GaleriasDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Galerias for query {}", query);
        Page<Galerias> result = galeriasSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(galeriasMapper::toDto);
    }
}
