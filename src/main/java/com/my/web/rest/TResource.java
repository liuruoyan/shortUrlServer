package com.my.web.rest;

import com.my.config.ApplicationProperties;
import com.my.domain.UrlMapping;
import com.my.service.UrlMappingQueryService;
import com.my.service.UrlMappingService;
import com.my.service.dto.UrlMappingCriteria;
import com.my.service.util.ShortUrlGenerator;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/t")
public class TResource {

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private UrlMappingQueryService urlMappingQueryService;

    @Autowired
    private ApplicationProperties applicationProperties;


    @GetMapping("/{shortCode}")
    public String redirctToLongUrl(@PathVariable String shortCode) {

        UrlMappingCriteria criteria = new UrlMappingCriteria();
        StringFilter stringFilter = new StringFilter();
        stringFilter.setEquals(applicationProperties.getBaseUrl() + shortCode);
        criteria.setUrlShort(stringFilter);

        List<UrlMapping> longUrlList = urlMappingQueryService.findByCriteria(criteria);

        return "redirect:" + longUrlList.get(0).getUrlLong();
    }

}
