package com.coastee.server.oauth.domain;

public interface SocialTokens {

    String getAccessToken();

    String getRefreshToken();
}
