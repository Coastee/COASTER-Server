package com.coastee.server.login.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthTokens {
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    public static AuthTokens of(
            final String accessToken,
            final String refreshToken
    ) {
        return new AuthTokens("Bearer", accessToken, refreshToken);
    }
}
