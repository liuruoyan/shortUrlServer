package com.my.repository;

import com.my.domain.UrlMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UrlMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>, JpaSpecificationExecutor<UrlMapping> {

}
