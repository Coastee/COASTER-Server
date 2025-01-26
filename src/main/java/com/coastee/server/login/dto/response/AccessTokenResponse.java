package com.coastee.server.login.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AccessTokenResponse {
    private String accessToken;

    @Builder(builderMethodName = "of")
    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
