package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.ExpedicionService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.ExpedicionDTO;
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
 * REST controller for managing Expedicion.
 */
@RestController
@RequestMapping("/api")
public class ExpedicionResource {

    private final Logger log = LoggerFactory.getLogger(ExpedicionResource.class);

    private static final String ENTITY_NAME = "expedicion";

    private final ExpedicionService expedicionService;

    public ExpedicionResource(ExpedicionService expedicionService) {
        this.expedicionService = expedicionService;
    }

    /**
     * POST  /expedicions : Create a new expedicion.
     *
     * @param expedicionDTO the expedicionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expedicionDTO, or with status 400 (Bad Request) if the expedicion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/expedicions")
    @Timed
    public ResponseEntity<ExpedicionDTO> createExpedicion(@Valid @RequestBody ExpedicionDTO expedicionDTO) throws URISyntaxException {
        log.debug("REST request to save Expedicion : {}", expedicionDTO);
        if (expedicionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new expedicion cannot already have an ID")).body(null);
        }
        ExpedicionDTO result = expedicionService.save(expedicionDTO);
        return ResponseEntity.created(new URI("/api/expedicions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expedicions : Updates an existing expedicion.
     *
     * @param expedicionDTO the expedicionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expedicionDTO,
     * or with status 400 (Bad Request) if the expedicionDTO is not valid,
     * or with status 500 (Internal Server Error) if the expedicionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/expedicions")
    @Timed
    public ResponseEntity<ExpedicionDTO> updateExpedicion(@Valid @RequestBody ExpedicionDTO expedicionDTO) throws URISyntaxException {
        log.debug("REST request to update Expedicion : {}", expedicionDTO);
        if (expedicionDTO.getId() == null) {
            return createExpedicion(expedicionDTO);
        }
        ExpedicionDTO result = expedicionService.save(expedicionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, expedicionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expedicions : get all the expedicions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of expedicions in body
     */
    @GetMapping("/expedicions")
    @Timed
    public ResponseEntity<List<ExpedicionDTO>> getAllExpedicions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Expedicions");
        Page<ExpedicionDTO> page = expedicionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expedicions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expedicions/:id : get the "id" expedicion.
     *
     * @param id the id of the expedicionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expedicionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/expedicions/{id}")
    @Timed
    public ResponseEntity<ExpedicionDTO> getExpedicion(@PathVariable Long id) {
        log.debug("REST request to get Expedicion : {}", id);
        ExpedicionDTO expedicionDTO = expedicionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(expedicionDTO));
    }

    /**
     * DELETE  /expedicions/:id : delete the "id" expedicion.
     *
     * @param id the id of the expedicionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/expedicions/{id}")
    @Timed
    public ResponseEntity<Void> deleteExpedicion(@PathVariable Long id) {
        log.debug("REST request to delete Expedicion : {}", id);
        expedicionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/expedicions?query=:query : search for the expedicion corresponding
     * to the query.
     *
     * @param query the query of the expedicion search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/expedicions")
    @Timed
    public ResponseEntity<List<ExpedicionDTO>> searchExpedicions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Expedicions for query {}", query);
        Page<ExpedicionDTO> page = expedicionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/expedicions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
