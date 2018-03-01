package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.NivelService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.NivelDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Nivel.
 */
@RestController
@RequestMapping("/api")
public class NivelResource {

    private final Logger log = LoggerFactory.getLogger(NivelResource.class);

    private static final String ENTITY_NAME = "nivel";

    private final NivelService nivelService;

    public NivelResource(NivelService nivelService) {
        this.nivelService = nivelService;
    }

    /**
     * POST  /nivels : Create a new nivel.
     *
     * @param nivelDTO the nivelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nivelDTO, or with status 400 (Bad Request) if the nivel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nivels")
    @Timed
    public ResponseEntity<NivelDTO> createNivel(@Valid @RequestBody NivelDTO nivelDTO) throws URISyntaxException {
        log.debug("REST request to save Nivel : {}", nivelDTO);
        if (nivelDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new nivel cannot already have an ID")).body(null);
        }
        NivelDTO result = nivelService.save(nivelDTO);
        return ResponseEntity.created(new URI("/api/nivels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nivels : Updates an existing nivel.
     *
     * @param nivelDTO the nivelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nivelDTO,
     * or with status 400 (Bad Request) if the nivelDTO is not valid,
     * or with status 500 (Internal Server Error) if the nivelDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nivels")
    @Timed
    public ResponseEntity<NivelDTO> updateNivel(@Valid @RequestBody NivelDTO nivelDTO) throws URISyntaxException {
        log.debug("REST request to update Nivel : {}", nivelDTO);
        if (nivelDTO.getId() == null) {
            return createNivel(nivelDTO);
        }
        NivelDTO result = nivelService.save(nivelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nivelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nivels : get all the nivels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of nivels in body
     */
    @GetMapping("/nivels")
    @Timed
    public ResponseEntity<List<NivelDTO>> getAllNivels(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Nivels");
        Page<NivelDTO> page = nivelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nivels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /nivels/:id : get the "id" nivel.
     *
     * @param id the id of the nivelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nivelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/nivels/{id}")
    @Timed
    public ResponseEntity<NivelDTO> getNivel(@PathVariable Long id) {
        log.debug("REST request to get Nivel : {}", id);
        NivelDTO nivelDTO = nivelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nivelDTO));
    }

    /**
     * DELETE  /nivels/:id : delete the "id" nivel.
     *
     * @param id the id of the nivelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nivels/{id}")
    @Timed
    public ResponseEntity<Void> deleteNivel(@PathVariable Long id) {
        log.debug("REST request to delete Nivel : {}", id);
        nivelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/nivels?query=:query : search for the nivel corresponding
     * to the query.
     *
     * @param query the query of the nivel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/nivels")
    @Timed
    public ResponseEntity<List<NivelDTO>> searchNivels(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Nivels for query {}", query);
        Page<NivelDTO> page = nivelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/nivels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
