package com.coastee.server.oauth.client;

import com.coastee.server.global.util.PropertyUtil;
import com.coastee.server.oauth.domain.NaverSocialTokens;
import com.coastee.server.oauth.domain.OAuthLoginParams;
import com.coastee.server.oauth.domain.SocialTokens;
import com.coastee.server.oauth.dto.info.OAuthInfoResponse;
import com.coastee.server.user.domain.SocialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "naverApiClient", url = "https://nid.naver.com")
public interface NaverApiClient extends OAuthApiClient {
    URI OAUTH_INFO_BASEURL = URI.create("https://openapi.naver.com");

    @Override
    default SocialTokens requestAccessToken(final OAuthLoginParams params) {
        params.updateClientId(PropertyUtil.getProperty("login.naver.client_id"));
        params.updateClientSecret(PropertyUtil.getProperty("login.naver.client_secret"));
        return requestToken(params);
    }

    @GetMapping(value = "/oauth2.0/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    NaverSocialTokens requestToken(@ModelAttribute final OAuthLoginParams params);


    @Override
    default OAuthInfoResponse requestOAuthInfo(final SocialTokens socialTokens) {
        return requestOAuthInfo(socialTokens.getAccessToken(), OAUTH_INFO_BASEURL);
    }

    @GetMapping(value = "/v1/nid/me")
    OAuthInfoResponse requestOAuthInfo(
            @RequestHeader final String accessToken,
            final URI uri
    );

    @Override
    default SocialType socialType() {
        return SocialType.NAVER;
    }
}