package com.coastee.server.login.client;

import com.coastee.server.global.util.PropertyUtil;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.login.infrastructure.socialtokens.NaverSocialTokens;
import com.coastee.server.login.infrastructure.userinfo.NaverUserInfo;
import com.coastee.server.user.domain.SocialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "naverApiClient", url = "https://nid.naver.com")
public interface NaverApiClient extends OAuthApiClient {
    URI OAUTH_INFO_BASEURL = URI.create("https://openapi.naver.com");

    @Override
    default SocialTokens requestAccessToken(final OAuthLoginParams params) {
        params.updateClientId(PropertyUtil.getProperty("login.naver.client-id"));
        params.updateClientSecret(PropertyUtil.getProperty("login.naver.client-secret"));
        return requestAccessToken((NaverLoginParams) params);
    }

    @GetMapping(value = "/oauth2.0/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    NaverSocialTokens requestAccessToken(@SpringQueryMap final NaverLoginParams params);

    @Override
    default OAuthUserInfo requestOAuthInfo(final SocialTokens socialTokens) {
        return requestOAuthInfo(ACCESS_TOKEN_HEADER + socialTokens.getAccessToken(), OAUTH_INFO_BASEURL);
    }

    @GetMapping(value = "/v1/nid/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    NaverUserInfo requestOAuthInfo(
            @RequestHeader("Authorization") final String accessToken,
            final URI uri
    );

    @Override
    default SocialType socialType() {
        return SocialType.NAVER;
    }
}