package com.coastee.server.user.dto.request;

import com.coastee.server.global.dto.UpdateRequest;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.UserIntro;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class UserUpdateRequest extends UpdateRequest {
    @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 10자 이하로 설정할 수 있습니다.")
    private String nickname;

    @Size(max = 5, message = "url은 최대 5개까지 설정할 수 있습니다.")
    private List<String> urlList;

    @Size(max = 20, message = "한줄 소개는 최대 20자까지 설정할 수 있습니다.")
    private String headline;

    @Size(max = 10, message = "직업은 최대 10자까지 설정할 수 있습니다.")
    private String job;

    private Integer expYears;

    @Size(max = 60, message = "소개는 최대 60자까지 설정할 수 있습니다.")
    private String bio;

    public void validateNullValue(final User user) {
        this.nickname = getOrDefault(this.nickname, user.getNickname());
        this.urlList = getOrDefault(this.urlList, user.getUrlList());
        UserIntro userIntro = user.getUserIntro();
        this.headline = getOrDefault(this.headline, userIntro.getHeadline());
        this.job = getOrDefault(this.job, userIntro.getJob());
        this.expYears = getOrDefault(this.expYears, userIntro.getExpYears());
        this.bio = getOrDefault(this.bio, user.getBio());
    }
}
