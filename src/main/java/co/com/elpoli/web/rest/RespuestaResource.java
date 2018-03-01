package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.RespuestaService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.RespuestaDTO;
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
 * REST controller for managing Respuesta.
 */
@RestController
@RequestMapping("/api")
public class RespuestaResource {

    private final Logger log = LoggerFactory.getLogger(RespuestaResource.class);

    private static final String ENTITY_NAME = "respuesta";

    private final RespuestaService respuestaService;

    public RespuestaResource(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    /**
     * POST  /respuestas : Create a new respuesta.
     *
     * @param respuestaDTO the respuestaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new respuestaDTO, or with status 400 (Bad Request) if the respuesta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/respuestas")
    @Timed
    public ResponseEntity<RespuestaDTO> createRespuesta(@Valid @RequestBody RespuestaDTO respuestaDTO) throws URISyntaxException {
        log.debug("REST request to save Respuesta : {}", respuestaDTO);
        if (respuestaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new respuesta cannot already have an ID")).body(null);
        }
        RespuestaDTO result = respuestaService.save(respuestaDTO);
        return ResponseEntity.created(new URI("/api/respuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /respuestas : Updates an existing respuesta.
     *
     * @param respuestaDTO the respuestaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated respuestaDTO,
     * or with status 400 (Bad Request) if the respuestaDTO is not valid,
     * or with status 500 (Internal Server Error) if the respuestaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/respuestas")
    @Timed
    public ResponseEntity<RespuestaDTO> updateRespuesta(@Valid @RequestBody RespuestaDTO respuestaDTO) throws URISyntaxException {
        log.debug("REST request to update Respuesta : {}", respuestaDTO);
        if (respuestaDTO.getId() == null) {
            return createRespuesta(respuestaDTO);
        }
        RespuestaDTO result = respuestaService.save(respuestaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, respuestaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /respuestas : get all the respuestas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of respuestas in body
     */
    @GetMapping("/respuestas")
    @Timed
    public ResponseEntity<List<RespuestaDTO>> getAllRespuestas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Respuestas");
        Page<RespuestaDTO> page = respuestaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/respuestas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /respuestas/:id : get the "id" respuesta.
     *
     * @param id the id of the respuestaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the respuestaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/respuestas/{id}")
    @Timed
    public ResponseEntity<RespuestaDTO> getRespuesta(@PathVariable Long id) {
        log.debug("REST request to get Respuesta : {}", id);
        RespuestaDTO respuestaDTO = respuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(respuestaDTO));
    }

    /**
     * DELETE  /respuestas/:id : delete the "id" respuesta.
     *
     * @param id the id of the respuestaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/respuestas/{id}")
    @Timed
    public ResponseEntity<Void> deleteRespuesta(@PathVariable Long id) {
        log.debug("REST request to delete Respuesta : {}", id);
        respuestaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/respuestas?query=:query : search for the respuesta corresponding
     * to the query.
     *
     * @param query the query of the respuesta search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/respuestas")
    @Timed
    public ResponseEntity<List<RespuestaDTO>> searchRespuestas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Respuestas for query {}", query);
        Page<RespuestaDTO> page = respuestaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/respuestas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
