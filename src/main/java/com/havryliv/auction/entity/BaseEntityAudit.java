package com.havryliv.auction.entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Base entity audit
 *
 * @author Cem Ikta
 */
@MappedSuperclass
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityAudit {

    @CreatedDate
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @CreatedBy
    @Size(max = 20)
    @Column(name = "created_by", length = 20)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @LastModifiedBy
    @Size(max = 20)
    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    /**
     * Sets createdAt before insert
     */
    @PrePersist
    public void setCreationDate() {
        this.createdAt = new Date();
    }

    /**
     * Sets updatedAt before update
     */
    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = new Date();
    }

}
