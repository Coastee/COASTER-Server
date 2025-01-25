package com.coastee.server.login.infrastructure.socialtokens;

import com.coastee.server.login.domain.SocialTokens;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleSocialTokens implements SocialTokens {
    private String scope;
    private int expiresIn;
    private String accessToken;
    private String numberOfEmployees;

    @Override
    public String getRefreshToken() {
        return "";
    }
}
