package com.my.web.rest;

import com.my.domain.UrlMapping;
import com.my.service.UrlMappingService;
import com.my.web.rest.errors.BadRequestAlertException;
import com.my.service.dto.UrlMappingCriteria;
import com.my.service.UrlMappingQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.my.domain.UrlMapping}.
 */
@RestController
@RequestMapping("/api")
public class UrlMappingResource {

    private final Logger log = LoggerFactory.getLogger(UrlMappingResource.class);

    private static final String ENTITY_NAME = "urlMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UrlMappingService urlMappingService;

    private final UrlMappingQueryService urlMappingQueryService;

    public UrlMappingResource(UrlMappingService urlMappingService, UrlMappingQueryService urlMappingQueryService) {
        this.urlMappingService = urlMappingService;
        this.urlMappingQueryService = urlMappingQueryService;
    }

    /**
     * {@code POST  /url-mappings} : Create a new urlMapping.
     *
     * @param urlMapping the urlMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new urlMapping, or with status {@code 400 (Bad Request)} if the urlMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/url-mappings")
    public ResponseEntity<UrlMapping> createUrlMapping(@RequestBody UrlMapping urlMapping) throws URISyntaxException {
        log.debug("REST request to save UrlMapping : {}", urlMapping);
        if (urlMapping.getId() != null) {
            throw new BadRequestAlertException("A new urlMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UrlMapping result = urlMappingService.save(urlMapping);
        return ResponseEntity.created(new URI("/api/url-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /url-mappings} : Updates an existing urlMapping.
     *
     * @param urlMapping the urlMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urlMapping,
     * or with status {@code 400 (Bad Request)} if the urlMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the urlMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/url-mappings")
    public ResponseEntity<UrlMapping> updateUrlMapping(@RequestBody UrlMapping urlMapping) throws URISyntaxException {
        log.debug("REST request to update UrlMapping : {}", urlMapping);
        if (urlMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UrlMapping result = urlMappingService.save(urlMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urlMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /url-mappings} : get all the urlMappings.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of urlMappings in body.
     */
    @GetMapping("/url-mappings")
    public ResponseEntity<List<UrlMapping>> getAllUrlMappings(UrlMappingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UrlMappings by criteria: {}", criteria);
        Page<UrlMapping> page = urlMappingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /url-mappings/count} : count all the urlMappings.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/url-mappings/count")
    public ResponseEntity<Long> countUrlMappings(UrlMappingCriteria criteria) {
        log.debug("REST request to count UrlMappings by criteria: {}", criteria);
        return ResponseEntity.ok().body(urlMappingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /url-mappings/:id} : get the "id" urlMapping.
     *
     * @param id the id of the urlMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the urlMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/url-mappings/{id}")
    public ResponseEntity<UrlMapping> getUrlMapping(@PathVariable Long id) {
        log.debug("REST request to get UrlMapping : {}", id);
        Optional<UrlMapping> urlMapping = urlMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(urlMapping);
    }

    /**
     * {@code DELETE  /url-mappings/:id} : delete the "id" urlMapping.
     *
     * @param id the id of the urlMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/url-mappings/{id}")
    public ResponseEntity<Void> deleteUrlMapping(@PathVariable Long id) {
        log.debug("REST request to delete UrlMapping : {}", id);
        urlMappingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
