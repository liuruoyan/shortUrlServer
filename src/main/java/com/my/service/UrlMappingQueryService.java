package com.my.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.my.domain.UrlMapping;
import com.my.domain.*; // for static metamodels
import com.my.repository.UrlMappingRepository;
import com.my.service.dto.UrlMappingCriteria;

/**
 * Service for executing complex queries for {@link UrlMapping} entities in the database.
 * The main input is a {@link UrlMappingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UrlMapping} or a {@link Page} of {@link UrlMapping} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UrlMappingQueryService extends QueryService<UrlMapping> {

    private final Logger log = LoggerFactory.getLogger(UrlMappingQueryService.class);

    private final UrlMappingRepository urlMappingRepository;

    public UrlMappingQueryService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    /**
     * Return a {@link List} of {@link UrlMapping} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UrlMapping> findByCriteria(UrlMappingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UrlMapping> specification = createSpecification(criteria);
        return urlMappingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UrlMapping} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UrlMapping> findByCriteria(UrlMappingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UrlMapping> specification = createSpecification(criteria);
        return urlMappingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UrlMappingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UrlMapping> specification = createSpecification(criteria);
        return urlMappingRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<UrlMapping> createSpecification(UrlMappingCriteria criteria) {
        Specification<UrlMapping> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UrlMapping_.id));
            }
            if (criteria.getUrlLong() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlLong(), UrlMapping_.urlLong));
            }
            if (criteria.getUrlShort() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlShort(), UrlMapping_.urlShort));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), UrlMapping_.type));
            }
        }
        return specification;
    }
}
