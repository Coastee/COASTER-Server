package com.coastee.server.login.domain;

import com.coastee.server.user.domain.SocialType;

public interface OAuthLoginParams {

    String GRANT_TYPE = "authorization_code";

    SocialType socialType();

    void updateClientId(final String clientId);

    void updateClientSecret(final String clientSecret);

    void updateRedirectUri(final String uri);
}
