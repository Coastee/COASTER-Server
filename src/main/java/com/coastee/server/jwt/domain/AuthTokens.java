package com.coastee.server.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;

    public static AuthTokens of(
            final String accessToken,
            final String refreshToken
    ) {
        return new AuthTokens(accessToken, refreshToken);
    }
}
