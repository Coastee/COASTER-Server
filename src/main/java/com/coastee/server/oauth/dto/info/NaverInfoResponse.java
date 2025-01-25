package com.coastee.server.oauth.dto.info;

import com.coastee.server.user.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {
    private String resultcode;
    private String message;
    private NaverInfoDetail response;

    @Override
    public String getSocialId() {
        return response.getSocialId();
    }

    @Override
    public String getName() {
        return response.getName();
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }
}

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class NaverInfoDetail {
    @JsonProperty("id")
    private String socialId;
    private String name;
    private String email;
}
