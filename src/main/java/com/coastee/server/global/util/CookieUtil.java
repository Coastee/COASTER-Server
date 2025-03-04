package com.coastee.server.global.util;

import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.infrastructure.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Component
public class CookieUtil {

    public void setAuthCookie(
            final HttpServletResponse response,
            final AuthTokens authTokens
    ) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", authTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(JwtProvider.refreshTokenExpirationTime / 1000L)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token", authTokens.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(JwtProvider.accessTokenExpirationTime / 1000L)
                .path("/")
                .sameSite("None")
                .build();

        response.addHeader(SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(SET_COOKIE, accessTokenCookie.toString());
    }
}
