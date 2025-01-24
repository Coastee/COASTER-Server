package com.coastee.server.oauth.service;

import com.coastee.server.jwt.util.JwtProvider;
import com.coastee.server.oauth.domain.OAuthLoginParams;
import com.coastee.server.oauth.dto.info.OAuthInfoResponse;
import com.coastee.server.oauth.dto.response.OAuthUserResponse;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtProvider jwtProvider;

    public OAuthUserResponse login(final OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        User user = userRepository
                .findBySocialId(oAuthInfoResponse.getSocialId())
                .orElse(null);
//        if (user == null) {
//
//        }
//
//        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
//        user.updateRefreshToken(authTokens.getRefreshToken());
        return null;
    }
}
