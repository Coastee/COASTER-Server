package com.coastee.server.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.coastee.server.global.BaseEntityStatus.ACTIVE;
import static com.coastee.server.global.BaseEntityStatus.DELETED;
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

    public void toggleCurrentStatus() {
        if (this.status.equals(ACTIVE)) {
            this.status = DELETED;
        } else {
            this.status = ACTIVE;
        }
    }

    public void updateStatus(final BaseEntityStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.status.equals(ACTIVE);
    }
}
