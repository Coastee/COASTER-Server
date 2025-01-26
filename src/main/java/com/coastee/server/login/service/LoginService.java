package com.coastee.server.login.service;

import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthTokens login(final OAuthLoginParams params) {
        OAuthUserInfo userInfo = requestOAuthInfoService.request(params);
        User user = findOrCreateUser(userInfo);
        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
        user.updateRefreshToken(tokens.getRefreshToken());
        return tokens;
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

    public String refreshToken(final String accessToken, final String refreshToken) {
        if (jwtProvider.isValidRefreshAndExpiredAccess(refreshToken, accessToken)) {
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new InvalidJwtException(INVALID_REFRESH_TOKEN));
            return jwtProvider.createAccessToken(user.getId().toString());
        }
        if (jwtProvider.isValidRefreshAndValidAccess(refreshToken, accessToken)) {
            return accessToken;
        }
        throw new InvalidJwtException(FAIL_VALIDATE_TOKEN);
    }
}
