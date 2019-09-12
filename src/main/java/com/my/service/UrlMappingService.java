package com.my.service;

import com.my.domain.UrlMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link UrlMapping}.
 */
public interface UrlMappingService {

    /**
     * Save a urlMapping.
     *
     * @param urlMapping the entity to save.
     * @return the persisted entity.
     */
    UrlMapping save(UrlMapping urlMapping);

    /**
     * Get all the urlMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UrlMapping> findAll(Pageable pageable);


    /**
     * Get the "id" urlMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UrlMapping> findOne(Long id);

    /**
     * Delete the "id" urlMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
