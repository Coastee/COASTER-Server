package com.coastee.server.oauth.domain;

import com.coastee.server.user.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverLoginParams implements OAuthLoginParams {
    @JsonProperty("grant_type")
    private String grantType = "authorization_code";

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("code")
    private String code;

    @JsonProperty("state")
    private String state = "STATE_STRING";

    @Override
    public SocialType socialType() {
        return SocialType.NAVER;
    }

    public NaverLoginParams(
            final String code
    ) {
        this.code = code;
    }

    @Override
    public void updateClientId(final String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void updateClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
