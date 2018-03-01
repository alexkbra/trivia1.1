package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.PistaService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.PistaDTO;
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
 * REST controller for managing Pista.
 */
@RestController
@RequestMapping("/api")
public class PistaResource {

    private final Logger log = LoggerFactory.getLogger(PistaResource.class);

    private static final String ENTITY_NAME = "pista";

    private final PistaService pistaService;

    public PistaResource(PistaService pistaService) {
        this.pistaService = pistaService;
    }

    /**
     * POST  /pistas : Create a new pista.
     *
     * @param pistaDTO the pistaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pistaDTO, or with status 400 (Bad Request) if the pista has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pistas")
    @Timed
    public ResponseEntity<PistaDTO> createPista(@Valid @RequestBody PistaDTO pistaDTO) throws URISyntaxException {
        log.debug("REST request to save Pista : {}", pistaDTO);
        if (pistaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pista cannot already have an ID")).body(null);
        }
        PistaDTO result = pistaService.save(pistaDTO);
        return ResponseEntity.created(new URI("/api/pistas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pistas : Updates an existing pista.
     *
     * @param pistaDTO the pistaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pistaDTO,
     * or with status 400 (Bad Request) if the pistaDTO is not valid,
     * or with status 500 (Internal Server Error) if the pistaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pistas")
    @Timed
    public ResponseEntity<PistaDTO> updatePista(@Valid @RequestBody PistaDTO pistaDTO) throws URISyntaxException {
        log.debug("REST request to update Pista : {}", pistaDTO);
        if (pistaDTO.getId() == null) {
            return createPista(pistaDTO);
        }
        PistaDTO result = pistaService.save(pistaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pistaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pistas : get all the pistas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pistas in body
     */
    @GetMapping("/pistas")
    @Timed
    public ResponseEntity<List<PistaDTO>> getAllPistas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pistas");
        Page<PistaDTO> page = pistaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pistas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pistas/:id : get the "id" pista.
     *
     * @param id the id of the pistaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pistaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pistas/{id}")
    @Timed
    public ResponseEntity<PistaDTO> getPista(@PathVariable Long id) {
        log.debug("REST request to get Pista : {}", id);
        PistaDTO pistaDTO = pistaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pistaDTO));
    }

    /**
     * DELETE  /pistas/:id : delete the "id" pista.
     *
     * @param id the id of the pistaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pistas/{id}")
    @Timed
    public ResponseEntity<Void> deletePista(@PathVariable Long id) {
        log.debug("REST request to delete Pista : {}", id);
        pistaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pistas?query=:query : search for the pista corresponding
     * to the query.
     *
     * @param query the query of the pista search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pistas")
    @Timed
    public ResponseEntity<List<PistaDTO>> searchPistas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pistas for query {}", query);
        Page<PistaDTO> page = pistaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pistas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
