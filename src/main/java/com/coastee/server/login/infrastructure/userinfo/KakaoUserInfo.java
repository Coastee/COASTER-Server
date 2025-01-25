package com.coastee.server.login.infrastructure.userinfo;

import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfo implements OAuthUserInfo {
    private KakaoAccount kakaoAccount;
    private String id;

    @Override
    public String getSocialId() {
        return this.id;
    }

    @Override
    public String getName() {
        return kakaoAccount.getProfile().getNickname();
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class KakaoAccount {
    private KakaoProfile profile;
}

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class KakaoProfile {
    private String nickname;
}