package com.my.web.rest;

import com.my.config.ApplicationProperties;
import com.my.domain.UrlMapping;
import com.my.service.UrlMappingQueryService;
import com.my.service.UrlMappingService;
import com.my.service.dto.UrlMappingCriteria;
import com.my.service.dto.UserDTO;
import com.my.service.util.ShortUrlGenerator;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ShortenResource {

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private ApplicationProperties applicationProperties;

//    @GetMapping("/short_url/shorten")
//    public ResponseEntity<UrlMapping> getShortUrl(String url_long) {
//        String shortUrl = ShortUrlGenerator.shortUrl(url_long);
//        UrlMapping urlMapping = new UrlMapping();
//        urlMapping.setUrlLong(url_long);
//        String url_short = applicationProperties.getBaseUrl() + shortUrl;
//        urlMapping.setUrlShort(url_short);
//
//        try {
//            urlMappingService.save(urlMapping);
//        }catch(Exception e){
//
//        }
//
//        return ResponseEntity.ok(urlMapping);
//    }


    @GetMapping("/short_url/shorten")
    public ResponseEntity<List<UrlMapping>> getShortUrl(String[] url_long) {

        List<UrlMapping> mappingList = new ArrayList<>(url_long.length);

        for (int i = 0; i < url_long.length; i++) {
            String shortUrl = ShortUrlGenerator.shortUrl(url_long[i]);
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setUrlLong(url_long[i]);
            String url_short = applicationProperties.getBaseUrl() + shortUrl;
            urlMapping.setUrlShort(url_short);

            try {
                urlMappingService.save(urlMapping);
            }catch(DataIntegrityViolationException e){
                // UK重复异常
            }

            mappingList.add(urlMapping);
        }

        return ResponseEntity.ok(mappingList);
    }

}
