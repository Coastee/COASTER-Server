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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;

    public ExperienceElement(final Experience experience) {
        this.id = experience.getId();
        this.title = experience.getTitle();
        this.startDate = experience.getStartDate();
        this.endDate = experience.getEndDate();
        this.content = experience.getContent();
    }
}
