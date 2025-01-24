package com.coastee.server.oauth.client;

import com.coastee.server.oauth.domain.OAuthLoginParams;
import com.coastee.server.oauth.domain.SocialTokens;
import com.coastee.server.oauth.dto.info.OAuthInfoResponse;
import com.coastee.server.user.domain.SocialType;

public interface OAuthApiClient {

    SocialType socialType();

    SocialTokens requestAccessToken(OAuthLoginParams params);

    OAuthInfoResponse requestOAuthInfo(SocialTokens socialTokens);
}
