package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PeriodElement {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public PeriodElement(final Period period) {
        this.startDate = period.getStartDate();
        this.endDate = period.getEndDate();
    }
}
