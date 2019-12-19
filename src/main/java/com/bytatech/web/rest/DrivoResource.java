package com.bytatech.web.rest;
import com.bytatech.service.DrivoService;
import com.bytatech.web.rest.errors.BadRequestAlertException;
import com.bytatech.web.rest.util.HeaderUtil;
import com.bytatech.web.rest.util.PaginationUtil;
import com.bytatech.service.dto.DrivoDTO;
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
 * REST controller for managing Drivo.
 */
@RestController
@RequestMapping("/api")
public class DrivoResource {

    private final Logger log = LoggerFactory.getLogger(DrivoResource.class);

    private static final String ENTITY_NAME = "elasticbatchDrivo";

    private final DrivoService drivoService;

    public DrivoResource(DrivoService drivoService) {
        this.drivoService = drivoService;
    }

    /**
     * POST  /drivos : Create a new drivo.
     *
     * @param drivoDTO the drivoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new drivoDTO, or with status 400 (Bad Request) if the drivo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/drivos")
    public ResponseEntity<DrivoDTO> createDrivo(@RequestBody DrivoDTO drivoDTO) throws URISyntaxException {
        log.debug("REST request to save Drivo : {}", drivoDTO);
        if (drivoDTO.getId() != null) {
            throw new BadRequestAlertException("A new drivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DrivoDTO result = drivoService.save(drivoDTO);
        return ResponseEntity.created(new URI("/api/drivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /drivos : Updates an existing drivo.
     *
     * @param drivoDTO the drivoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated drivoDTO,
     * or with status 400 (Bad Request) if the drivoDTO is not valid,
     * or with status 500 (Internal Server Error) if the drivoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/drivos")
    public ResponseEntity<DrivoDTO> updateDrivo(@RequestBody DrivoDTO drivoDTO) throws URISyntaxException {
        log.debug("REST request to update Drivo : {}", drivoDTO);
        if (drivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DrivoDTO result = drivoService.save(drivoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, drivoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /drivos : get all the drivos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of drivos in body
     */
    @GetMapping("/drivos")
    public ResponseEntity<List<DrivoDTO>> getAllDrivos(Pageable pageable) {
        log.debug("REST request to get a page of Drivos");
        Page<DrivoDTO> page = drivoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/drivos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /drivos/:id : get the "id" drivo.
     *
     * @param id the id of the drivoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the drivoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/drivos/{id}")
    public ResponseEntity<DrivoDTO> getDrivo(@PathVariable Long id) {
        log.debug("REST request to get Drivo : {}", id);
        Optional<DrivoDTO> drivoDTO = drivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(drivoDTO);
    }

    /**
     * DELETE  /drivos/:id : delete the "id" drivo.
     *
     * @param id the id of the drivoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/drivos/{id}")
    public ResponseEntity<Void> deleteDrivo(@PathVariable Long id) {
        log.debug("REST request to delete Drivo : {}", id);
        drivoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/drivos?query=:query : search for the drivo corresponding
     * to the query.
     *
     * @param query the query of the drivo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/drivos")
    public ResponseEntity<List<DrivoDTO>> searchDrivos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Drivos for query {}", query);
        Page<DrivoDTO> page = drivoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/drivos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
