package com.my.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.my.domain.UrlMapping} entity. This class is used
 * in {@link com.my.web.rest.UrlMappingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /url-mappings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UrlMappingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter urlLong;

    private StringFilter urlShort;

    private IntegerFilter type;

    public UrlMappingCriteria(){
    }

    public UrlMappingCriteria(UrlMappingCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.urlLong = other.urlLong == null ? null : other.urlLong.copy();
        this.urlShort = other.urlShort == null ? null : other.urlShort.copy();
        this.type = other.type == null ? null : other.type.copy();
    }

    @Override
    public UrlMappingCriteria copy() {
        return new UrlMappingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUrlLong() {
        return urlLong;
    }

    public void setUrlLong(StringFilter urlLong) {
        this.urlLong = urlLong;
    }

    public StringFilter getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(StringFilter urlShort) {
        this.urlShort = urlShort;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UrlMappingCriteria that = (UrlMappingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(urlLong, that.urlLong) &&
            Objects.equals(urlShort, that.urlShort) &&
            Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        urlLong,
        urlShort,
        type
        );
    }

    @Override
    public String toString() {
        return "UrlMappingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (urlLong != null ? "urlLong=" + urlLong + ", " : "") +
                (urlShort != null ? "urlShort=" + urlShort + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
            "}";
    }

}
