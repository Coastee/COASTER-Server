package com.coastee.server.oauth.domain;

import com.coastee.server.user.domain.SocialType;
import lombok.Data;

@Data
public class NaverLoginParams implements OAuthLoginParams {
    private String grant_type = "authorization_code";
    private String client_id;
    private String client_secret;
    private String code;
    private String state = "STATE_STRING";
    private String service_provider = "NAVER";

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
        this.client_id = clientId;
    }

    @Override
    public void updateClientSecret(final String clientSecret) {
        this.client_secret = clientSecret;
    }
}
