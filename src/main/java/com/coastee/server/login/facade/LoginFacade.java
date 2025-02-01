package com.coastee.server.login.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.coastee.server.login.infrastructure.loginparams.LinkedInLoginParams;
import com.coastee.server.login.service.OAuthInfoService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_VALIDATE_TOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginFacade {
    private final OAuthInfoService oAuthInfoService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthTokens login(final OAuthLoginParams params) {
        OAuthUserInfo userInfo = oAuthInfoService.request(params);
        User user = userService.findOrCreateUser(userInfo);
        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
        user.updateRefreshToken(tokens.getRefreshToken());
        return tokens;
    }

    public void connect(
            final Accessor accessor,
            final LinkedInLoginParams params
    ) {
        User user = userService.findById(accessor.getUserId());
        OAuthUserInfo userInfo = oAuthInfoService.request(params);
        user.verify(userInfo.getSocialId());
    }

    public String renewalToken(final String accessToken, final String refreshToken) {
        if (jwtProvider.isValidRefreshAndExpiredAccess(refreshToken, accessToken)) {
            User user = userService.findByRefreshToken(refreshToken);
            return jwtProvider.createAccessToken(user.getId().toString());
        }
        if (jwtProvider.isValidRefreshAndValidAccess(refreshToken, accessToken)) {
            return accessToken;
        }
        throw new InvalidJwtException(FAIL_VALIDATE_TOKEN);
    }
}
