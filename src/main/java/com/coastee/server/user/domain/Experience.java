package com.coastee.server.user.domain;

import com.coastee.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Experience extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String content;

    @Builder(builderMethodName = "of")
    public Experience(
            final String title,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final String content
    ) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }
}
