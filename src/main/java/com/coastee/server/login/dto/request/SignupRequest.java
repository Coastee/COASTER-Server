package com.coastee.server.login.dto.request;

import com.coastee.server.user.dto.request.UserUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class SignupRequest extends UserUpdateRequest {
    private List<Long> serverIdList;

    public SignupRequest(
            final String nickname,
            final List<String> urlList,
            final String headline,
            final String job,
            final Integer expYears,
            final String bio,
            final List<Long> serverIdList) {
        super(nickname, urlList, headline, job, expYears, bio);
        this.serverIdList = serverIdList;
    }
}
