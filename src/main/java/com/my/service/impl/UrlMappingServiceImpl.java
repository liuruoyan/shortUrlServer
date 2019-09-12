package com.my.service.impl;

import com.my.service.UrlMappingService;
import com.my.domain.UrlMapping;
import com.my.repository.UrlMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UrlMapping}.
 */
@Service
@Transactional
public class UrlMappingServiceImpl implements UrlMappingService {

    private final Logger log = LoggerFactory.getLogger(UrlMappingServiceImpl.class);

    private final UrlMappingRepository urlMappingRepository;

    public UrlMappingServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    /**
     * Save a urlMapping.
     *
     * @param urlMapping the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UrlMapping save(UrlMapping urlMapping) {
        log.debug("Request to save UrlMapping : {}", urlMapping);
        return urlMappingRepository.save(urlMapping);
    }

    /**
     * Get all the urlMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UrlMapping> findAll(Pageable pageable) {
        log.debug("Request to get all UrlMappings");
        return urlMappingRepository.findAll(pageable);
    }


    /**
     * Get one urlMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UrlMapping> findOne(Long id) {
        log.debug("Request to get UrlMapping : {}", id);
        return urlMappingRepository.findById(id);
    }

    /**
     * Delete the urlMapping by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UrlMapping : {}", id);
        urlMappingRepository.deleteById(id);
    }
}
