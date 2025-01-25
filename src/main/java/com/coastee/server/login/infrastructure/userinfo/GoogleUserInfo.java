package com.coastee.server.login.infrastructure.userinfo;

import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserInfo implements OAuthUserInfo {
    public String id;
    public String name;
    public String email;

    @Override
    public String getSocialId() {
        return this.id;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE;
    }
}
