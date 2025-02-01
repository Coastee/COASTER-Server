package com.coastee.server.chatroom.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
