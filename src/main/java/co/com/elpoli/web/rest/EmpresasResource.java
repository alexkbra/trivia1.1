package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.EmpresasService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.EmpresasDTO;
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
 * REST controller for managing Empresas.
 */
@RestController
@RequestMapping("/api")
public class EmpresasResource {

    private final Logger log = LoggerFactory.getLogger(EmpresasResource.class);

    private static final String ENTITY_NAME = "empresas";

    private final EmpresasService empresasService;

    public EmpresasResource(EmpresasService empresasService) {
        this.empresasService = empresasService;
    }

    /**
     * POST  /empresas : Create a new empresas.
     *
     * @param empresasDTO the empresasDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empresasDTO, or with status 400 (Bad Request) if the empresas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empresas")
    @Timed
    public ResponseEntity<EmpresasDTO> createEmpresas(@Valid @RequestBody EmpresasDTO empresasDTO) throws URISyntaxException {
        log.debug("REST request to save Empresas : {}", empresasDTO);
        if (empresasDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new empresas cannot already have an ID")).body(null);
        }
        EmpresasDTO result = empresasService.save(empresasDTO);
        return ResponseEntity.created(new URI("/api/empresas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empresas : Updates an existing empresas.
     *
     * @param empresasDTO the empresasDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empresasDTO,
     * or with status 400 (Bad Request) if the empresasDTO is not valid,
     * or with status 500 (Internal Server Error) if the empresasDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empresas")
    @Timed
    public ResponseEntity<EmpresasDTO> updateEmpresas(@Valid @RequestBody EmpresasDTO empresasDTO) throws URISyntaxException {
        log.debug("REST request to update Empresas : {}", empresasDTO);
        if (empresasDTO.getId() == null) {
            return createEmpresas(empresasDTO);
        }
        EmpresasDTO result = empresasService.save(empresasDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empresasDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empresas : get all the empresas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of empresas in body
     */
    @GetMapping("/empresas")
    @Timed
    public ResponseEntity<List<EmpresasDTO>> getAllEmpresas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Empresas");
        Page<EmpresasDTO> page = empresasService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/empresas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /empresas/:id : get the "id" empresas.
     *
     * @param id the id of the empresasDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empresasDTO, or with status 404 (Not Found)
     */
    @GetMapping("/empresas/{id}")
    @Timed
    public ResponseEntity<EmpresasDTO> getEmpresas(@PathVariable Long id) {
        log.debug("REST request to get Empresas : {}", id);
        EmpresasDTO empresasDTO = empresasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(empresasDTO));
    }

    /**
     * DELETE  /empresas/:id : delete the "id" empresas.
     *
     * @param id the id of the empresasDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empresas/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmpresas(@PathVariable Long id) {
        log.debug("REST request to delete Empresas : {}", id);
        empresasService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/empresas?query=:query : search for the empresas corresponding
     * to the query.
     *
     * @param query the query of the empresas search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/empresas")
    @Timed
    public ResponseEntity<List<EmpresasDTO>> searchEmpresas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Empresas for query {}", query);
        Page<EmpresasDTO> page = empresasService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/empresas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
