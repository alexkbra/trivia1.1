package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.PreguntaService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.PreguntaDTO;
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
 * REST controller for managing Pregunta.
 */
@RestController
@RequestMapping("/api")
public class PreguntaResource {

    private final Logger log = LoggerFactory.getLogger(PreguntaResource.class);

    private static final String ENTITY_NAME = "pregunta";

    private final PreguntaService preguntaService;

    public PreguntaResource(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    /**
     * POST  /preguntas : Create a new pregunta.
     *
     * @param preguntaDTO the preguntaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preguntaDTO, or with status 400 (Bad Request) if the pregunta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/preguntas")
    @Timed
    public ResponseEntity<PreguntaDTO> createPregunta(@Valid @RequestBody PreguntaDTO preguntaDTO) throws URISyntaxException {
        log.debug("REST request to save Pregunta : {}", preguntaDTO);
        if (preguntaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pregunta cannot already have an ID")).body(null);
        }
        PreguntaDTO result = preguntaService.save(preguntaDTO);
        return ResponseEntity.created(new URI("/api/preguntas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preguntas : Updates an existing pregunta.
     *
     * @param preguntaDTO the preguntaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preguntaDTO,
     * or with status 400 (Bad Request) if the preguntaDTO is not valid,
     * or with status 500 (Internal Server Error) if the preguntaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/preguntas")
    @Timed
    public ResponseEntity<PreguntaDTO> updatePregunta(@Valid @RequestBody PreguntaDTO preguntaDTO) throws URISyntaxException {
        log.debug("REST request to update Pregunta : {}", preguntaDTO);
        if (preguntaDTO.getId() == null) {
            return createPregunta(preguntaDTO);
        }
        PreguntaDTO result = preguntaService.save(preguntaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preguntaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preguntas : get all the preguntas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of preguntas in body
     */
    @GetMapping("/preguntas")
    @Timed
    public ResponseEntity<List<PreguntaDTO>> getAllPreguntas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Preguntas");
        Page<PreguntaDTO> page = preguntaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/preguntas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /preguntas/:id : get the "id" pregunta.
     *
     * @param id the id of the preguntaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preguntaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/preguntas/{id}")
    @Timed
    public ResponseEntity<PreguntaDTO> getPregunta(@PathVariable Long id) {
        log.debug("REST request to get Pregunta : {}", id);
        PreguntaDTO preguntaDTO = preguntaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(preguntaDTO));
    }

    /**
     * DELETE  /preguntas/:id : delete the "id" pregunta.
     *
     * @param id the id of the preguntaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/preguntas/{id}")
    @Timed
    public ResponseEntity<Void> deletePregunta(@PathVariable Long id) {
        log.debug("REST request to delete Pregunta : {}", id);
        preguntaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/preguntas?query=:query : search for the pregunta corresponding
     * to the query.
     *
     * @param query the query of the pregunta search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/preguntas")
    @Timed
    public ResponseEntity<List<PreguntaDTO>> searchPreguntas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Preguntas for query {}", query);
        Page<PreguntaDTO> page = preguntaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/preguntas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
