package com.coastee.server.user.dto.request;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.user.domain.Experience;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceUpdateRequest {
    @Size(min = 1)
    private String title;

    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public void validateNullValue(final Experience experience) {
        this.title = getOrDefault(this.title, experience.getTitle());
        this.content = getOrDefault(this.content, experience.getContent());
        Period period = experience.getPeriod();
        this.startDate = getOrDefault(this.startDate, period.getStartDate());
        this.endDate = getOrDefault(this.endDate, period.getEndDate());
    }

    private <T> T getOrDefault(
            final T newValue,
            final T defaultValue
    ) {
        if (newValue != null)
            return newValue;
        return defaultValue;
    }
}
