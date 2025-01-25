package com.coastee.server.oauth.service;

import com.coastee.server.login.jwt.domain.AuthTokens;
import com.coastee.server.login.jwt.util.JwtProvider;
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
        User user = findOrCreateUser(oAuthInfoResponse);
        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
        user.updateRefreshToken(tokens.getRefreshToken());
        return OAuthUserResponse.of()
                .userId(user.getId())
                .authTokens(tokens)
                .build();
    }

    private User findOrCreateUser(final OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findBySocialId(oAuthInfoResponse.getSocialId())
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private User newUser(final OAuthInfoResponse oAuthInfoResponse) {
        User user = User.of()
                .name(oAuthInfoResponse.getName())
                .email(oAuthInfoResponse.getEmail())
                .socialType(oAuthInfoResponse.getSocialType())
                .socialId(oAuthInfoResponse.getSocialId())
                .build();
        return userRepository.save(user);
    }
}
