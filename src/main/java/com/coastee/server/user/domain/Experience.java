package com.coastee.server.user.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class Experience implements Serializable {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
}
