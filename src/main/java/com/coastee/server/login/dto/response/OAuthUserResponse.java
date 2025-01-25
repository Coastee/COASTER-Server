package com.coastee.server.login.dto.response;

import com.coastee.server.login.domain.AuthTokens;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class OAuthUserResponse {
    private Long userId;
    private AuthTokens authTokens;

    @Builder(builderMethodName = "of")
    public OAuthUserResponse(
            final Long userId,
            final AuthTokens authTokens
    ) {
        this.userId = userId;
        this.authTokens = authTokens;
    }
}
