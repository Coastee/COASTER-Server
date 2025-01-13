package com.coastee.server.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class Experience implements Serializable {
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
