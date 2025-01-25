package com.coastee.server.login.domain;

import com.coastee.server.user.domain.SocialType;

public interface OAuthLoginParams {

    SocialType socialType();

    void updateClientId(final String clientId);

    void updateClientSecret(final String clientSecret);
}
