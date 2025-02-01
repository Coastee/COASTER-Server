package com.coastee.server.login.infrastructure.userinfo;

import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.HashMap;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LinkedInUserInfo implements OAuthUserInfo {
    private String sub;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private HashMap<String, String> locale;
    private String email;
    private Boolean emailVerified;

    @Override
    public String getSocialId() {
        return this.sub;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.LINKEDIN;
    }
}
