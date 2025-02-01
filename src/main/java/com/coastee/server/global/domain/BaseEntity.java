package com.coastee.server.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.coastee.server.global.domain.BaseEntityStatus.*;
import static jakarta.persistence.EnumType.STRING;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Enumerated(STRING)
    private BaseEntityStatus status;

    public BaseEntity() {
        this.status = ACTIVE;
    }

    public void activate() {
        this.status = ACTIVE;
    }

    public void delete() {
        this.status = DELETED;
    }

    public boolean isActive() {
        return this.status.equals(ACTIVE);
    }

    public boolean isDeleted() {
        return this.status.equals(INACTIVE);
    }
}
