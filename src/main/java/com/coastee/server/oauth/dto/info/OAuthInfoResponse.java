package com.coastee.server.oauth.dto.info;

import com.coastee.server.user.domain.SocialType;

public interface OAuthInfoResponse {

    String getSocialId();

    String getName();

    String getEmail();

    SocialType getSocialType();
}
