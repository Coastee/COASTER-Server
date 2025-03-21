package com.coastee.server.login.client;

import com.coastee.server.global.util.PropertyUtil;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.socialtokens.KakaoSocialTokens;
import com.coastee.server.login.infrastructure.userinfo.KakaoUserInfo;
import com.coastee.server.user.domain.SocialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(value = "kakaoApiClient", url = "https://kauth.kakao.com")
public interface KakaoApiClient extends OAuthApiClient {
    URI OAUTH_INFO_BASEURL = URI.create("https://kapi.kakao.com");

    @Override
    default SocialTokens requestAccessToken(final OAuthLoginParams params) {
        params.updateClientId(PropertyUtil.getProperty("login.kakao.client-id"));
        return requestAccessToken((KakaoLoginParams) params);
    }

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoSocialTokens requestAccessToken(@RequestBody final KakaoLoginParams params);

    @Override
    default OAuthUserInfo requestOAuthInfo(final SocialTokens socialTokens) {
        return requestOAuthInfo(
                ACCESS_TOKEN_HEADER + socialTokens.getAccessToken(),
                OAUTH_INFO_BASEURL
        );
    }

    @GetMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserInfo requestOAuthInfo(
            @RequestHeader("Authorization") final String accessToken,
            final URI uri
    );

    @Override
    default SocialType socialType() {
        return SocialType.KAKAO;
    }
}
