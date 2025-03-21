package com.coastee.server.login.domain;

import lombok.*;

import static com.coastee.server.global.domain.Constant.TOKEN_PREFIX;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthTokens {
    private String tokenType = TOKEN_PREFIX;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    @Setter
    private boolean newUser;

    @Builder(builderMethodName = "of")
    public AuthTokens(
            final String subject,
            final String accessToken,
            final String refreshToken
    ) {
        this.userId = Long.valueOf(subject);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
