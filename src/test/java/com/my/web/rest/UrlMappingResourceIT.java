package com.my.web.rest;

import com.my.ShortUrlServerApp;
import com.my.domain.UrlMapping;
import com.my.repository.UrlMappingRepository;
import com.my.service.UrlMappingService;
import com.my.web.rest.errors.ExceptionTranslator;
import com.my.service.dto.UrlMappingCriteria;
import com.my.service.UrlMappingQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.my.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UrlMappingResource} REST controller.
 */
@SpringBootTest(classes = ShortUrlServerApp.class)
public class UrlMappingResourceIT {

    private static final String DEFAULT_URL_LONG = "AAAAAAAAAA";
    private static final String UPDATED_URL_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_URL_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_URL_SHORT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final Integer SMALLER_TYPE = 1 - 1;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private UrlMappingQueryService urlMappingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUrlMappingMockMvc;

    private UrlMapping urlMapping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UrlMappingResource urlMappingResource = new UrlMappingResource(urlMappingService, urlMappingQueryService);
        this.restUrlMappingMockMvc = MockMvcBuilders.standaloneSetup(urlMappingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlMapping createEntity(EntityManager em) {
        UrlMapping urlMapping = new UrlMapping()
            .urlLong(DEFAULT_URL_LONG)
            .urlShort(DEFAULT_URL_SHORT)
            .type(DEFAULT_TYPE);
        return urlMapping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlMapping createUpdatedEntity(EntityManager em) {
        UrlMapping urlMapping = new UrlMapping()
            .urlLong(UPDATED_URL_LONG)
            .urlShort(UPDATED_URL_SHORT)
            .type(UPDATED_TYPE);
        return urlMapping;
    }

    @BeforeEach
    public void initTest() {
        urlMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createUrlMapping() throws Exception {
        int databaseSizeBeforeCreate = urlMappingRepository.findAll().size();

        // Create the UrlMapping
        restUrlMappingMockMvc.perform(post("/api/url-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlMapping)))
            .andExpect(status().isCreated());

        // Validate the UrlMapping in the database
        List<UrlMapping> urlMappingList = urlMappingRepository.findAll();
        assertThat(urlMappingList).hasSize(databaseSizeBeforeCreate + 1);
        UrlMapping testUrlMapping = urlMappingList.get(urlMappingList.size() - 1);
        assertThat(testUrlMapping.getUrlLong()).isEqualTo(DEFAULT_URL_LONG);
        assertThat(testUrlMapping.getUrlShort()).isEqualTo(DEFAULT_URL_SHORT);
        assertThat(testUrlMapping.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createUrlMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = urlMappingRepository.findAll().size();

        // Create the UrlMapping with an existing ID
        urlMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrlMappingMockMvc.perform(post("/api/url-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        List<UrlMapping> urlMappingList = urlMappingRepository.findAll();
        assertThat(urlMappingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUrlMappings() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList
        restUrlMappingMockMvc.perform(get("/api/url-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urlMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlLong").value(hasItem(DEFAULT_URL_LONG.toString())))
            .andExpect(jsonPath("$.[*].urlShort").value(hasItem(DEFAULT_URL_SHORT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getUrlMapping() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get the urlMapping
        restUrlMappingMockMvc.perform(get("/api/url-mappings/{id}", urlMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(urlMapping.getId().intValue()))
            .andExpect(jsonPath("$.urlLong").value(DEFAULT_URL_LONG.toString()))
            .andExpect(jsonPath("$.urlShort").value(DEFAULT_URL_SHORT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlLongIsEqualToSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlLong equals to DEFAULT_URL_LONG
        defaultUrlMappingShouldBeFound("urlLong.equals=" + DEFAULT_URL_LONG);

        // Get all the urlMappingList where urlLong equals to UPDATED_URL_LONG
        defaultUrlMappingShouldNotBeFound("urlLong.equals=" + UPDATED_URL_LONG);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlLongIsInShouldWork() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlLong in DEFAULT_URL_LONG or UPDATED_URL_LONG
        defaultUrlMappingShouldBeFound("urlLong.in=" + DEFAULT_URL_LONG + "," + UPDATED_URL_LONG);

        // Get all the urlMappingList where urlLong equals to UPDATED_URL_LONG
        defaultUrlMappingShouldNotBeFound("urlLong.in=" + UPDATED_URL_LONG);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlLongIsNullOrNotNull() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlLong is not null
        defaultUrlMappingShouldBeFound("urlLong.specified=true");

        // Get all the urlMappingList where urlLong is null
        defaultUrlMappingShouldNotBeFound("urlLong.specified=false");
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlShortIsEqualToSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlShort equals to DEFAULT_URL_SHORT
        defaultUrlMappingShouldBeFound("urlShort.equals=" + DEFAULT_URL_SHORT);

        // Get all the urlMappingList where urlShort equals to UPDATED_URL_SHORT
        defaultUrlMappingShouldNotBeFound("urlShort.equals=" + UPDATED_URL_SHORT);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlShortIsInShouldWork() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlShort in DEFAULT_URL_SHORT or UPDATED_URL_SHORT
        defaultUrlMappingShouldBeFound("urlShort.in=" + DEFAULT_URL_SHORT + "," + UPDATED_URL_SHORT);

        // Get all the urlMappingList where urlShort equals to UPDATED_URL_SHORT
        defaultUrlMappingShouldNotBeFound("urlShort.in=" + UPDATED_URL_SHORT);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByUrlShortIsNullOrNotNull() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where urlShort is not null
        defaultUrlMappingShouldBeFound("urlShort.specified=true");

        // Get all the urlMappingList where urlShort is null
        defaultUrlMappingShouldNotBeFound("urlShort.specified=false");
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type equals to DEFAULT_TYPE
        defaultUrlMappingShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the urlMappingList where type equals to UPDATED_TYPE
        defaultUrlMappingShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultUrlMappingShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the urlMappingList where type equals to UPDATED_TYPE
        defaultUrlMappingShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type is not null
        defaultUrlMappingShouldBeFound("type.specified=true");

        // Get all the urlMappingList where type is null
        defaultUrlMappingShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type is greater than or equal to DEFAULT_TYPE
        defaultUrlMappingShouldBeFound("type.greaterThanOrEqual=" + DEFAULT_TYPE);

        // Get all the urlMappingList where type is greater than or equal to UPDATED_TYPE
        defaultUrlMappingShouldNotBeFound("type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type is less than or equal to DEFAULT_TYPE
        defaultUrlMappingShouldBeFound("type.lessThanOrEqual=" + DEFAULT_TYPE);

        // Get all the urlMappingList where type is less than or equal to SMALLER_TYPE
        defaultUrlMappingShouldNotBeFound("type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type is less than DEFAULT_TYPE
        defaultUrlMappingShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the urlMappingList where type is less than UPDATED_TYPE
        defaultUrlMappingShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllUrlMappingsByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        urlMappingRepository.saveAndFlush(urlMapping);

        // Get all the urlMappingList where type is greater than DEFAULT_TYPE
        defaultUrlMappingShouldNotBeFound("type.greaterThan=" + DEFAULT_TYPE);

        // Get all the urlMappingList where type is greater than SMALLER_TYPE
        defaultUrlMappingShouldBeFound("type.greaterThan=" + SMALLER_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUrlMappingShouldBeFound(String filter) throws Exception {
        restUrlMappingMockMvc.perform(get("/api/url-mappings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urlMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].urlLong").value(hasItem(DEFAULT_URL_LONG)))
            .andExpect(jsonPath("$.[*].urlShort").value(hasItem(DEFAULT_URL_SHORT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restUrlMappingMockMvc.perform(get("/api/url-mappings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUrlMappingShouldNotBeFound(String filter) throws Exception {
        restUrlMappingMockMvc.perform(get("/api/url-mappings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUrlMappingMockMvc.perform(get("/api/url-mappings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUrlMapping() throws Exception {
        // Get the urlMapping
        restUrlMappingMockMvc.perform(get("/api/url-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUrlMapping() throws Exception {
        // Initialize the database
        urlMappingService.save(urlMapping);

        int databaseSizeBeforeUpdate = urlMappingRepository.findAll().size();

        // Update the urlMapping
        UrlMapping updatedUrlMapping = urlMappingRepository.findById(urlMapping.getId()).get();
        // Disconnect from session so that the updates on updatedUrlMapping are not directly saved in db
        em.detach(updatedUrlMapping);
        updatedUrlMapping
            .urlLong(UPDATED_URL_LONG)
            .urlShort(UPDATED_URL_SHORT)
            .type(UPDATED_TYPE);

        restUrlMappingMockMvc.perform(put("/api/url-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUrlMapping)))
            .andExpect(status().isOk());

        // Validate the UrlMapping in the database
        List<UrlMapping> urlMappingList = urlMappingRepository.findAll();
        assertThat(urlMappingList).hasSize(databaseSizeBeforeUpdate);
        UrlMapping testUrlMapping = urlMappingList.get(urlMappingList.size() - 1);
        assertThat(testUrlMapping.getUrlLong()).isEqualTo(UPDATED_URL_LONG);
        assertThat(testUrlMapping.getUrlShort()).isEqualTo(UPDATED_URL_SHORT);
        assertThat(testUrlMapping.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingUrlMapping() throws Exception {
        int databaseSizeBeforeUpdate = urlMappingRepository.findAll().size();

        // Create the UrlMapping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrlMappingMockMvc.perform(put("/api/url-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UrlMapping in the database
        List<UrlMapping> urlMappingList = urlMappingRepository.findAll();
        assertThat(urlMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUrlMapping() throws Exception {
        // Initialize the database
        urlMappingService.save(urlMapping);

        int databaseSizeBeforeDelete = urlMappingRepository.findAll().size();

        // Delete the urlMapping
        restUrlMappingMockMvc.perform(delete("/api/url-mappings/{id}", urlMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UrlMapping> urlMappingList = urlMappingRepository.findAll();
        assertThat(urlMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrlMapping.class);
        UrlMapping urlMapping1 = new UrlMapping();
        urlMapping1.setId(1L);
        UrlMapping urlMapping2 = new UrlMapping();
        urlMapping2.setId(urlMapping1.getId());
        assertThat(urlMapping1).isEqualTo(urlMapping2);
        urlMapping2.setId(2L);
        assertThat(urlMapping1).isNotEqualTo(urlMapping2);
        urlMapping1.setId(null);
        assertThat(urlMapping1).isNotEqualTo(urlMapping2);
    }
}
