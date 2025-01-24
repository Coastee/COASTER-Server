package com.coastee.server.oauth.service;

import com.coastee.server.oauth.client.OAuthApiClient;
import com.coastee.server.oauth.domain.OAuthLoginParams;
import com.coastee.server.oauth.domain.SocialTokens;
import com.coastee.server.oauth.dto.info.OAuthInfoResponse;
import com.coastee.server.user.domain.SocialType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestOAuthInfoService {
    private Map<SocialType, OAuthApiClient> clientMap;

    public RequestOAuthInfoService(final List<OAuthApiClient> clientList) {
        this.clientMap = clientList.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::socialType, Function.identity())
        );
    }

    public OAuthInfoResponse request(final OAuthLoginParams params) {
        OAuthApiClient client = clientMap.get(params.socialType());
        SocialTokens socialTokens = client.requestAccessToken(params);
        return client.requestOAuthInfo(socialTokens);
    }
}
