package com.coastee.server.login.client;

import com.coastee.server.global.util.PropertyUtil;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
import com.coastee.server.login.infrastructure.socialtokens.GoogleSocialTokens;
import com.coastee.server.login.infrastructure.userinfo.GoogleUserInfo;
import com.coastee.server.user.domain.SocialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleApiClient", url = "https://www.googleapis.com")
public interface GoogleApiClient extends OAuthApiClient {

    @Override
    default SocialTokens requestAccessToken(final OAuthLoginParams params) {
        params.updateClientId(PropertyUtil.getProperty("login.google.client-id"));
        params.updateClientSecret(PropertyUtil.getProperty("login.google.client-secret"));
        return requestAccessToken((GoogleLoginParams) params, (GoogleLoginParams) params);
    }

    @PostMapping(value = "/oauth2/v4/token")
    GoogleSocialTokens requestAccessToken(
            @SpringQueryMap final GoogleLoginParams params,
            @RequestBody final GoogleLoginParams body
    );

    @Override
    default OAuthUserInfo requestOAuthInfo(final SocialTokens socialTokens) {
        return requestOAuthInfo(socialTokens.getAccessToken());
    }

    @GetMapping(value = "/oauth2/v1/userinfo")
    GoogleUserInfo requestOAuthInfo(@RequestParam("access_token") String accessToken);


    @Override
    default SocialType socialType() {
        return SocialType.GOOGLE;
    }
}
