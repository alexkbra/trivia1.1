package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.PublicidadService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.PublicidadDTO;
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
 * REST controller for managing Publicidad.
 */
@RestController
@RequestMapping("/api")
public class PublicidadResource {

    private final Logger log = LoggerFactory.getLogger(PublicidadResource.class);

    private static final String ENTITY_NAME = "publicidad";

    private final PublicidadService publicidadService;

    public PublicidadResource(PublicidadService publicidadService) {
        this.publicidadService = publicidadService;
    }

    /**
     * POST  /publicidads : Create a new publicidad.
     *
     * @param publicidadDTO the publicidadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publicidadDTO, or with status 400 (Bad Request) if the publicidad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/publicidads")
    @Timed
    public ResponseEntity<PublicidadDTO> createPublicidad(@Valid @RequestBody PublicidadDTO publicidadDTO) throws URISyntaxException {
        log.debug("REST request to save Publicidad : {}", publicidadDTO);
        if (publicidadDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new publicidad cannot already have an ID")).body(null);
        }
        PublicidadDTO result = publicidadService.save(publicidadDTO);
        return ResponseEntity.created(new URI("/api/publicidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publicidads : Updates an existing publicidad.
     *
     * @param publicidadDTO the publicidadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publicidadDTO,
     * or with status 400 (Bad Request) if the publicidadDTO is not valid,
     * or with status 500 (Internal Server Error) if the publicidadDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publicidads")
    @Timed
    public ResponseEntity<PublicidadDTO> updatePublicidad(@Valid @RequestBody PublicidadDTO publicidadDTO) throws URISyntaxException {
        log.debug("REST request to update Publicidad : {}", publicidadDTO);
        if (publicidadDTO.getId() == null) {
            return createPublicidad(publicidadDTO);
        }
        PublicidadDTO result = publicidadService.save(publicidadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publicidadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publicidads : get all the publicidads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of publicidads in body
     */
    @GetMapping("/publicidads")
    @Timed
    public ResponseEntity<List<PublicidadDTO>> getAllPublicidads(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Publicidads");
        Page<PublicidadDTO> page = publicidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/publicidads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /publicidads/:id : get the "id" publicidad.
     *
     * @param id the id of the publicidadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publicidadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/publicidads/{id}")
    @Timed
    public ResponseEntity<PublicidadDTO> getPublicidad(@PathVariable Long id) {
        log.debug("REST request to get Publicidad : {}", id);
        PublicidadDTO publicidadDTO = publicidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(publicidadDTO));
    }

    /**
     * DELETE  /publicidads/:id : delete the "id" publicidad.
     *
     * @param id the id of the publicidadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/publicidads/{id}")
    @Timed
    public ResponseEntity<Void> deletePublicidad(@PathVariable Long id) {
        log.debug("REST request to delete Publicidad : {}", id);
        publicidadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/publicidads?query=:query : search for the publicidad corresponding
     * to the query.
     *
     * @param query the query of the publicidad search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/publicidads")
    @Timed
    public ResponseEntity<List<PublicidadDTO>> searchPublicidads(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Publicidads for query {}", query);
        Page<PublicidadDTO> page = publicidadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/publicidads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
