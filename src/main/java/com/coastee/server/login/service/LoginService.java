package com.coastee.server.login.service;

import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_VALIDATE_TOKEN;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_REFRESH_TOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

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
