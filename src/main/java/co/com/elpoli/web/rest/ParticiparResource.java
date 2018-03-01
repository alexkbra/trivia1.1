package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.ParticiparService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.ParticiparDTO;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Participar.
 */
@RestController
@RequestMapping("/api")
public class ParticiparResource {

    private final Logger log = LoggerFactory.getLogger(ParticiparResource.class);

    private static final String ENTITY_NAME = "participar";

    private final ParticiparService participarService;

    public ParticiparResource(ParticiparService participarService) {
        this.participarService = participarService;
    }

    /**
     * POST  /participars : Create a new participar.
     *
     * @param participarDTO the participarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participarDTO, or with status 400 (Bad Request) if the participar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/participars")
    @Timed
    public ResponseEntity<ParticiparDTO> createParticipar(@RequestBody ParticiparDTO participarDTO) throws URISyntaxException {
        log.debug("REST request to save Participar : {}", participarDTO);
        if (participarDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new participar cannot already have an ID")).body(null);
        }
        ParticiparDTO result = participarService.save(participarDTO);
        return ResponseEntity.created(new URI("/api/participars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participars : Updates an existing participar.
     *
     * @param participarDTO the participarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participarDTO,
     * or with status 400 (Bad Request) if the participarDTO is not valid,
     * or with status 500 (Internal Server Error) if the participarDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/participars")
    @Timed
    public ResponseEntity<ParticiparDTO> updateParticipar(@RequestBody ParticiparDTO participarDTO) throws URISyntaxException {
        log.debug("REST request to update Participar : {}", participarDTO);
        if (participarDTO.getId() == null) {
            return createParticipar(participarDTO);
        }
        ParticiparDTO result = participarService.save(participarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, participarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participars : get all the participars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of participars in body
     */
    @GetMapping("/participars")
    @Timed
    public ResponseEntity<List<ParticiparDTO>> getAllParticipars(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Participars");
        Page<ParticiparDTO> page = participarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/participars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /participars/:id : get the "id" participar.
     *
     * @param id the id of the participarDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participarDTO, or with status 404 (Not Found)
     */
    @GetMapping("/participars/{id}")
    @Timed
    public ResponseEntity<ParticiparDTO> getParticipar(@PathVariable Long id) {
        log.debug("REST request to get Participar : {}", id);
        ParticiparDTO participarDTO = participarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(participarDTO));
    }

    /**
     * DELETE  /participars/:id : delete the "id" participar.
     *
     * @param id the id of the participarDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/participars/{id}")
    @Timed
    public ResponseEntity<Void> deleteParticipar(@PathVariable Long id) {
        log.debug("REST request to delete Participar : {}", id);
        participarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/participars?query=:query : search for the participar corresponding
     * to the query.
     *
     * @param query the query of the participar search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/participars")
    @Timed
    public ResponseEntity<List<ParticiparDTO>> searchParticipars(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Participars for query {}", query);
        Page<ParticiparDTO> page = participarService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/participars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
