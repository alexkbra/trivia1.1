package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.CuestionarioService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.CuestionarioDTO;
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
 * REST controller for managing Cuestionario.
 */
@RestController
@RequestMapping("/api")
public class CuestionarioResource {

    private final Logger log = LoggerFactory.getLogger(CuestionarioResource.class);

    private static final String ENTITY_NAME = "cuestionario";

    private final CuestionarioService cuestionarioService;

    public CuestionarioResource(CuestionarioService cuestionarioService) {
        this.cuestionarioService = cuestionarioService;
    }

    /**
     * POST  /cuestionarios : Create a new cuestionario.
     *
     * @param cuestionarioDTO the cuestionarioDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cuestionarioDTO, or with status 400 (Bad Request) if the cuestionario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cuestionarios")
    @Timed
    public ResponseEntity<CuestionarioDTO> createCuestionario(@Valid @RequestBody CuestionarioDTO cuestionarioDTO) throws URISyntaxException {
        log.debug("REST request to save Cuestionario : {}", cuestionarioDTO);
        if (cuestionarioDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cuestionario cannot already have an ID")).body(null);
        }
        CuestionarioDTO result = cuestionarioService.save(cuestionarioDTO);
        return ResponseEntity.created(new URI("/api/cuestionarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cuestionarios : Updates an existing cuestionario.
     *
     * @param cuestionarioDTO the cuestionarioDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cuestionarioDTO,
     * or with status 400 (Bad Request) if the cuestionarioDTO is not valid,
     * or with status 500 (Internal Server Error) if the cuestionarioDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cuestionarios")
    @Timed
    public ResponseEntity<CuestionarioDTO> updateCuestionario(@Valid @RequestBody CuestionarioDTO cuestionarioDTO) throws URISyntaxException {
        log.debug("REST request to update Cuestionario : {}", cuestionarioDTO);
        if (cuestionarioDTO.getId() == null) {
            return createCuestionario(cuestionarioDTO);
        }
        CuestionarioDTO result = cuestionarioService.save(cuestionarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cuestionarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cuestionarios : get all the cuestionarios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cuestionarios in body
     */
    @GetMapping("/cuestionarios")
    @Timed
    public ResponseEntity<List<CuestionarioDTO>> getAllCuestionarios(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Cuestionarios");
        Page<CuestionarioDTO> page = cuestionarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuestionarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cuestionarios/:id : get the "id" cuestionario.
     *
     * @param id the id of the cuestionarioDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cuestionarioDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cuestionarios/{id}")
    @Timed
    public ResponseEntity<CuestionarioDTO> getCuestionario(@PathVariable Long id) {
        log.debug("REST request to get Cuestionario : {}", id);
        CuestionarioDTO cuestionarioDTO = cuestionarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cuestionarioDTO));
    }

    /**
     * DELETE  /cuestionarios/:id : delete the "id" cuestionario.
     *
     * @param id the id of the cuestionarioDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cuestionarios/{id}")
    @Timed
    public ResponseEntity<Void> deleteCuestionario(@PathVariable Long id) {
        log.debug("REST request to delete Cuestionario : {}", id);
        cuestionarioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cuestionarios?query=:query : search for the cuestionario corresponding
     * to the query.
     *
     * @param query the query of the cuestionario search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cuestionarios")
    @Timed
    public ResponseEntity<List<CuestionarioDTO>> searchCuestionarios(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Cuestionarios for query {}", query);
        Page<CuestionarioDTO> page = cuestionarioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cuestionarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
