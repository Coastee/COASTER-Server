package com.coastee.server.user.dto;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.user.domain.Experience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.coastee.server.global.domain.Constant.CURRENT_DATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceElement {
    private Long id;
    private String title;
    private List<String> contentList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ExperienceElement(final Experience experience) {
        this.id = experience.getId();
        this.title = experience.getTitle();
        this.contentList = experience.getContentList();
        Period period = experience.getPeriod();
        this.startDate = period.getStartDate();
        this.endDate = period.getEndDate();
        if (period.getEndDate().equals(CURRENT_DATE))
            this.endDate = null;
    }
}
