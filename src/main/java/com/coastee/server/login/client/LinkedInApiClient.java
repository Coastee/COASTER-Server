package com.coastee.server.login.client;

import com.coastee.server.global.util.PropertyUtil;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.infrastructure.loginparams.LinkedInLoginParams;
import com.coastee.server.login.infrastructure.socialtokens.LinkedInSocialTokens;
import com.coastee.server.login.infrastructure.userinfo.LinkedInUserInfo;
import com.coastee.server.user.domain.SocialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(value = "linkedinApiClient", url = "https://www.linkedin.com")
public interface LinkedInApiClient extends OAuthApiClient {
    URI OAUTH_INFO_BASEURL = URI.create("https://api.linkedin.com");

    @Override
    default SocialTokens requestAccessToken(final OAuthLoginParams params) {
        params.updateClientId(PropertyUtil.getProperty("login.linkedin.client-id"));
        params.updateClientSecret(PropertyUtil.getProperty("login.linkedin.client-secret"));
        return requestAccessToken((LinkedInLoginParams) params);
    }

    @PostMapping(value = "/oauth/v2/accessToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LinkedInSocialTokens requestAccessToken(@RequestBody final LinkedInLoginParams params);

    @Override
    default OAuthUserInfo requestOAuthInfo(final SocialTokens socialTokens) {
        return requestOAuthInfo(
                ACCESS_TOKEN_HEADER + socialTokens.getAccessToken(),
                OAUTH_INFO_BASEURL
        );
    }

    @GetMapping(value = "/v2/userinfo")
    LinkedInUserInfo requestOAuthInfo(
            @RequestHeader("Authorization") final String accessToken,
            final URI uri
    );

    @Override
    default SocialType socialType() {
        return SocialType.LINKEDIN;
    }
}
