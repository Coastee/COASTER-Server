package com.coastee.server.login.client;

import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.SocialType;

public interface OAuthApiClient {
    String ACCESS_TOKEN_HEADER = "Bearer ";

    SocialType socialType();

    SocialTokens requestAccessToken(final OAuthLoginParams params);

    OAuthUserInfo requestOAuthInfo(final SocialTokens socialTokens);
}
