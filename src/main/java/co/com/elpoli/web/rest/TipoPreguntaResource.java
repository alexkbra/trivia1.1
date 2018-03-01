package co.com.elpoli.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.elpoli.service.TipoPreguntaService;
import co.com.elpoli.web.rest.util.HeaderUtil;
import co.com.elpoli.web.rest.util.PaginationUtil;
import co.com.elpoli.service.dto.TipoPreguntaDTO;
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
 * REST controller for managing TipoPregunta.
 */
@RestController
@RequestMapping("/api")
public class TipoPreguntaResource {

    private final Logger log = LoggerFactory.getLogger(TipoPreguntaResource.class);

    private static final String ENTITY_NAME = "tipoPregunta";

    private final TipoPreguntaService tipoPreguntaService;

    public TipoPreguntaResource(TipoPreguntaService tipoPreguntaService) {
        this.tipoPreguntaService = tipoPreguntaService;
    }

    /**
     * POST  /tipo-preguntas : Create a new tipoPregunta.
     *
     * @param tipoPreguntaDTO the tipoPreguntaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoPreguntaDTO, or with status 400 (Bad Request) if the tipoPregunta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipo-preguntas")
    @Timed
    public ResponseEntity<TipoPreguntaDTO> createTipoPregunta(@Valid @RequestBody TipoPreguntaDTO tipoPreguntaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoPregunta : {}", tipoPreguntaDTO);
        if (tipoPreguntaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipoPregunta cannot already have an ID")).body(null);
        }
        TipoPreguntaDTO result = tipoPreguntaService.save(tipoPreguntaDTO);
        return ResponseEntity.created(new URI("/api/tipo-preguntas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-preguntas : Updates an existing tipoPregunta.
     *
     * @param tipoPreguntaDTO the tipoPreguntaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoPreguntaDTO,
     * or with status 400 (Bad Request) if the tipoPreguntaDTO is not valid,
     * or with status 500 (Internal Server Error) if the tipoPreguntaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipo-preguntas")
    @Timed
    public ResponseEntity<TipoPreguntaDTO> updateTipoPregunta(@Valid @RequestBody TipoPreguntaDTO tipoPreguntaDTO) throws URISyntaxException {
        log.debug("REST request to update TipoPregunta : {}", tipoPreguntaDTO);
        if (tipoPreguntaDTO.getId() == null) {
            return createTipoPregunta(tipoPreguntaDTO);
        }
        TipoPreguntaDTO result = tipoPreguntaService.save(tipoPreguntaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoPreguntaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-preguntas : get all the tipoPreguntas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tipoPreguntas in body
     */
    @GetMapping("/tipo-preguntas")
    @Timed
    public ResponseEntity<List<TipoPreguntaDTO>> getAllTipoPreguntas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TipoPreguntas");
        Page<TipoPreguntaDTO> page = tipoPreguntaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-preguntas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipo-preguntas/:id : get the "id" tipoPregunta.
     *
     * @param id the id of the tipoPreguntaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoPreguntaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tipo-preguntas/{id}")
    @Timed
    public ResponseEntity<TipoPreguntaDTO> getTipoPregunta(@PathVariable Long id) {
        log.debug("REST request to get TipoPregunta : {}", id);
        TipoPreguntaDTO tipoPreguntaDTO = tipoPreguntaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipoPreguntaDTO));
    }

    /**
     * DELETE  /tipo-preguntas/:id : delete the "id" tipoPregunta.
     *
     * @param id the id of the tipoPreguntaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipo-preguntas/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipoPregunta(@PathVariable Long id) {
        log.debug("REST request to delete TipoPregunta : {}", id);
        tipoPreguntaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipo-preguntas?query=:query : search for the tipoPregunta corresponding
     * to the query.
     *
     * @param query the query of the tipoPregunta search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tipo-preguntas")
    @Timed
    public ResponseEntity<List<TipoPreguntaDTO>> searchTipoPreguntas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TipoPreguntas for query {}", query);
        Page<TipoPreguntaDTO> page = tipoPreguntaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tipo-preguntas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
