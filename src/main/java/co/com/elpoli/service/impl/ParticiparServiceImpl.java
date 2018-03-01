package co.com.elpoli.service.impl;

import co.com.elpoli.service.ParticiparService;
import co.com.elpoli.domain.Participar;
import co.com.elpoli.repository.ParticiparRepository;
import co.com.elpoli.repository.search.ParticiparSearchRepository;
import co.com.elpoli.service.dto.ParticiparDTO;
import co.com.elpoli.service.mapper.ParticiparMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Participar.
 */
@Service
@Transactional
public class ParticiparServiceImpl implements ParticiparService{

    private final Logger log = LoggerFactory.getLogger(ParticiparServiceImpl.class);

    private final ParticiparRepository participarRepository;

    private final ParticiparMapper participarMapper;

    private final ParticiparSearchRepository participarSearchRepository;

    public ParticiparServiceImpl(ParticiparRepository participarRepository, ParticiparMapper participarMapper, ParticiparSearchRepository participarSearchRepository) {
        this.participarRepository = participarRepository;
        this.participarMapper = participarMapper;
        this.participarSearchRepository = participarSearchRepository;
    }

    /**
     * Save a participar.
     *
     * @param participarDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ParticiparDTO save(ParticiparDTO participarDTO) {
        log.debug("Request to save Participar : {}", participarDTO);
        Participar participar = participarMapper.toEntity(participarDTO);
        participar = participarRepository.save(participar);
        ParticiparDTO result = participarMapper.toDto(participar);
        participarSearchRepository.save(participar);
        return result;
    }

    /**
     *  Get all the participars.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParticiparDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participars");
        return participarRepository.findAll(pageable)
            .map(participarMapper::toDto);
    }

    /**
     *  Get one participar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ParticiparDTO findOne(Long id) {
        log.debug("Request to get Participar : {}", id);
        Participar participar = participarRepository.findOne(id);
        return participarMapper.toDto(participar);
    }

    /**
     *  Delete the  participar by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participar : {}", id);
        participarRepository.delete(id);
        participarSearchRepository.delete(id);
    }

    /**
     * Search for the participar corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParticiparDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Participars for query {}", query);
        Page<Participar> result = participarSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(participarMapper::toDto);
    }
}
