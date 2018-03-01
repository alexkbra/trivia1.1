package co.com.elpoli.service.impl;

import co.com.elpoli.service.ReglaService;
import co.com.elpoli.domain.Regla;
import co.com.elpoli.repository.ReglaRepository;
import co.com.elpoli.repository.search.ReglaSearchRepository;
import co.com.elpoli.service.dto.ReglaDTO;
import co.com.elpoli.service.mapper.ReglaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Regla.
 */
@Service
@Transactional
public class ReglaServiceImpl implements ReglaService{

    private final Logger log = LoggerFactory.getLogger(ReglaServiceImpl.class);

    private final ReglaRepository reglaRepository;

    private final ReglaMapper reglaMapper;

    private final ReglaSearchRepository reglaSearchRepository;

    public ReglaServiceImpl(ReglaRepository reglaRepository, ReglaMapper reglaMapper, ReglaSearchRepository reglaSearchRepository) {
        this.reglaRepository = reglaRepository;
        this.reglaMapper = reglaMapper;
        this.reglaSearchRepository = reglaSearchRepository;
    }

    /**
     * Save a regla.
     *
     * @param reglaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReglaDTO save(ReglaDTO reglaDTO) {
        log.debug("Request to save Regla : {}", reglaDTO);
        Regla regla = reglaMapper.toEntity(reglaDTO);
        regla = reglaRepository.save(regla);
        ReglaDTO result = reglaMapper.toDto(regla);
        reglaSearchRepository.save(regla);
        return result;
    }

    /**
     *  Get all the reglas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReglaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reglas");
        return reglaRepository.findAll(pageable)
            .map(reglaMapper::toDto);
    }

    /**
     *  Get one regla by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReglaDTO findOne(Long id) {
        log.debug("Request to get Regla : {}", id);
        Regla regla = reglaRepository.findOne(id);
        return reglaMapper.toDto(regla);
    }

    /**
     *  Delete the  regla by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Regla : {}", id);
        reglaRepository.delete(id);
        reglaSearchRepository.delete(id);
    }

    /**
     * Search for the regla corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReglaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reglas for query {}", query);
        Page<Regla> result = reglaSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(reglaMapper::toDto);
    }
}
