package com.coastee.server.user.domain;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus._INVALID_AUTHORITY;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
public class Experience extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> contentList = new ArrayList<>();

    @Embedded
    private Period period = new Period();

    public Experience(
            final User user,
            final String title,
            final List<String> contentList,
            final Period period
    ) {
        this.user = user;
        this.title = title;
        this.contentList = contentList;
        this.period = period;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Experience that = (Experience) o;
        return id != null && Objects.equals(id, that.id);
    }

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateContentList(final List<String> contentList) {
        this.contentList = contentList;
    }

    public void updatePeriod(
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        this.period = new Period(startDate, endDate);
    }

    public void validateUser(final User user) {
        if (this.user.equals(user)) return;
        throw new GeneralException(_INVALID_AUTHORITY);
    }
}
