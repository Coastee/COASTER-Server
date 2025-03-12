package com.coastee.server.user.dto;

import com.coastee.server.user.domain.UserIntro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class UserIntroElement {
    private String headline;
    private String job;
    private int expYears;

    public UserIntroElement(final UserIntro userIntro) {
        if (userIntro != null) {
            this.headline = userIntro.getHeadline();
            this.job = userIntro.getJob();
            this.expYears = userIntro.getExpYears();
        }
    }
}
