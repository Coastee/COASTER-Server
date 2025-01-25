package com.coastee.server.login.domain;

import com.coastee.server.user.domain.SocialType;

public interface OAuthUserInfo {

    String getSocialId();

    String getName();

    String getEmail();

    SocialType getSocialType();
}
