package com.coastee.server.user.domain;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

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

    private String content;

    @Embedded
    private Period period;

    public Experience(
            final User user,
            final String title,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final String content
    ) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.period = new Period(startDate, endDate);
    }
}
