package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.GaleriasService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.GaleriasDTO;
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
 * REST controller for managing Galerias.
 */
@RestController
@RequestMapping("/api")
public class GaleriasResource {

    private final Logger log = LoggerFactory.getLogger(GaleriasResource.class);

    private static final String ENTITY_NAME = "galerias";

    private final GaleriasService galeriasService;

    public GaleriasResource(GaleriasService galeriasService) {
        this.galeriasService = galeriasService;
    }

    /**
     * POST  /galerias : Create a new galerias.
     *
     * @param galeriasDTO the galeriasDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new galeriasDTO, or with status 400 (Bad Request) if the galerias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/galerias")
    @Timed
    public ResponseEntity<GaleriasDTO> createGalerias(@Valid @RequestBody GaleriasDTO galeriasDTO) throws URISyntaxException {
        log.debug("REST request to save Galerias : {}", galeriasDTO);
        if (galeriasDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new galerias cannot already have an ID")).body(null);
        }
        GaleriasDTO result = galeriasService.save(galeriasDTO);
        return ResponseEntity.created(new URI("/api/galerias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /galerias : Updates an existing galerias.
     *
     * @param galeriasDTO the galeriasDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated galeriasDTO,
     * or with status 400 (Bad Request) if the galeriasDTO is not valid,
     * or with status 500 (Internal Server Error) if the galeriasDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/galerias")
    @Timed
    public ResponseEntity<GaleriasDTO> updateGalerias(@Valid @RequestBody GaleriasDTO galeriasDTO) throws URISyntaxException {
        log.debug("REST request to update Galerias : {}", galeriasDTO);
        if (galeriasDTO.getId() == null) {
            return createGalerias(galeriasDTO);
        }
        GaleriasDTO result = galeriasService.save(galeriasDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, galeriasDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /galerias : get all the galerias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of galerias in body
     */
    @GetMapping("/galerias")
    @Timed
    public ResponseEntity<List<GaleriasDTO>> getAllGalerias(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Galerias");
        Page<GaleriasDTO> page = galeriasService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/galerias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /galerias/:id : get the "id" galerias.
     *
     * @param id the id of the galeriasDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the galeriasDTO, or with status 404 (Not Found)
     */
    @GetMapping("/galerias/{id}")
    @Timed
    public ResponseEntity<GaleriasDTO> getGalerias(@PathVariable Long id) {
        log.debug("REST request to get Galerias : {}", id);
        GaleriasDTO galeriasDTO = galeriasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(galeriasDTO));
    }

    /**
     * DELETE  /galerias/:id : delete the "id" galerias.
     *
     * @param id the id of the galeriasDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/galerias/{id}")
    @Timed
    public ResponseEntity<Void> deleteGalerias(@PathVariable Long id) {
        log.debug("REST request to delete Galerias : {}", id);
        galeriasService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/galerias?query=:query : search for the galerias corresponding
     * to the query.
     *
     * @param query the query of the galerias search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/galerias")
    @Timed
    public ResponseEntity<List<GaleriasDTO>> searchGalerias(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Galerias for query {}", query);
        Page<GaleriasDTO> page = galeriasService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/galerias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
