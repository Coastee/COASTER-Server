package com.coastee.server.login.service;

import com.coastee.server.login.client.OAuthApiClient;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.SocialTokens;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.SocialType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthInfoService {
    private Map<SocialType, OAuthApiClient> clientMap;

    public OAuthInfoService(final List<OAuthApiClient> clientList) {
        this.clientMap = clientList.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::socialType, Function.identity())
        );
    }

    public OAuthUserInfo request(final OAuthLoginParams params) {
        OAuthApiClient client = clientMap.get(params.socialType());
        SocialTokens socialTokens = client.requestAccessToken(params);
        return client.requestOAuthInfo(socialTokens);
    }
}
