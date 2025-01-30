package com.coastee.server.user.dto;

import com.coastee.server.user.domain.Experience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceElement {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ExperienceElement(final Experience experience) {
        this.id = experience.getId();
        this.title = experience.getTitle();
        this.content = experience.getContent();
        this.startDate = experience.getStartDate();
        this.endDate = experience.getEndDate();
    }
}
