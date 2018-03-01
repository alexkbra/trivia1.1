package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.ReglaService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.ReglaDTO;
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
 * REST controller for managing Regla.
 */
@RestController
@RequestMapping("/api")
public class ReglaResource {

    private final Logger log = LoggerFactory.getLogger(ReglaResource.class);

    private static final String ENTITY_NAME = "regla";

    private final ReglaService reglaService;

    public ReglaResource(ReglaService reglaService) {
        this.reglaService = reglaService;
    }

    /**
     * POST  /reglas : Create a new regla.
     *
     * @param reglaDTO the reglaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglaDTO, or with status 400 (Bad Request) if the regla has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reglas")
    @Timed
    public ResponseEntity<ReglaDTO> createRegla(@Valid @RequestBody ReglaDTO reglaDTO) throws URISyntaxException {
        log.debug("REST request to save Regla : {}", reglaDTO);
        if (reglaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new regla cannot already have an ID")).body(null);
        }
        ReglaDTO result = reglaService.save(reglaDTO);
        return ResponseEntity.created(new URI("/api/reglas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reglas : Updates an existing regla.
     *
     * @param reglaDTO the reglaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reglaDTO,
     * or with status 400 (Bad Request) if the reglaDTO is not valid,
     * or with status 500 (Internal Server Error) if the reglaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reglas")
    @Timed
    public ResponseEntity<ReglaDTO> updateRegla(@Valid @RequestBody ReglaDTO reglaDTO) throws URISyntaxException {
        log.debug("REST request to update Regla : {}", reglaDTO);
        if (reglaDTO.getId() == null) {
            return createRegla(reglaDTO);
        }
        ReglaDTO result = reglaService.save(reglaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reglaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reglas : get all the reglas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reglas in body
     */
    @GetMapping("/reglas")
    @Timed
    public ResponseEntity<List<ReglaDTO>> getAllReglas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Reglas");
        Page<ReglaDTO> page = reglaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reglas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reglas/:id : get the "id" regla.
     *
     * @param id the id of the reglaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reglas/{id}")
    @Timed
    public ResponseEntity<ReglaDTO> getRegla(@PathVariable Long id) {
        log.debug("REST request to get Regla : {}", id);
        ReglaDTO reglaDTO = reglaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reglaDTO));
    }

    /**
     * DELETE  /reglas/:id : delete the "id" regla.
     *
     * @param id the id of the reglaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reglas/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegla(@PathVariable Long id) {
        log.debug("REST request to delete Regla : {}", id);
        reglaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reglas?query=:query : search for the regla corresponding
     * to the query.
     *
     * @param query the query of the regla search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reglas")
    @Timed
    public ResponseEntity<List<ReglaDTO>> searchReglas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Reglas for query {}", query);
        Page<ReglaDTO> page = reglaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reglas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
