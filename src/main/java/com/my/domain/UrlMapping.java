package com.my.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A UrlMapping.
 */
@Entity
@Table(name = "url_mapping" ,indexes = {@Index(name = "UrlMapping_code_IDX", columnList = "url_short")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UrlMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_long")
    private String urlLong;

    @Column(name = "url_short")
    private String urlShort;

    @Column(name = "type")
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlLong() {
        return urlLong;
    }

    public UrlMapping urlLong(String urlLong) {
        this.urlLong = urlLong;
        return this;
    }

    public void setUrlLong(String urlLong) {
        this.urlLong = urlLong;
    }

    public String getUrlShort() {
        return urlShort;
    }

    public UrlMapping urlShort(String urlShort) {
        this.urlShort = urlShort;
        return this;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public Integer getType() {
        return type;
    }

    public UrlMapping type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrlMapping)) {
            return false;
        }
        return id != null && id.equals(((UrlMapping) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UrlMapping{" +
            "id=" + getId() +
            ", urlLong='" + getUrlLong() + "'" +
            ", urlShort='" + getUrlShort() + "'" +
            ", type=" + getType() +
            "}";
    }
}
