package com.coastee.server.login.service;

import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.dto.response.OAuthUserResponse;
import com.coastee.server.login.infrastructure.JwtProvider;
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
        OAuthUserInfo userInfo = requestOAuthInfoService.request(params);
        User user = findOrCreateUser(userInfo);
        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
        user.updateRefreshToken(tokens.getRefreshToken());
        return OAuthUserResponse.of()
                .userId(user.getId())
                .authTokens(tokens)
                .build();
    }

    private User findOrCreateUser(final OAuthUserInfo userInfo) {
        return userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> newUser(userInfo));
    }

    private User newUser(final OAuthUserInfo userInfo) {
        User user = User.of()
                .nickname(userInfo.getName())
                .email(userInfo.getEmail())
                .socialType(userInfo.getSocialType())
                .socialId(userInfo.getSocialId())
                .build();
        return userRepository.save(user);
    }
}
