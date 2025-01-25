package com.coastee.server.login.infrastructure;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.coastee.server.global.Constant.*;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_OAUTH_TOKEN;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NULL_TOKEN;

public class JwtHeaderUtil {

    public static String getAccessToken() {
        return getToken(HEADER_AUTHORIZATION);
    }

    public static String getAccessToken(final StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader(HEADER_AUTHORIZATION);
    }

    public static String getRefreshToken() {
        return getToken(HEADER_REFRESH_TOKEN);
    }

    private static String getToken(final String tokenHeader) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        String headerValue = request.getHeader(tokenHeader);
        if (headerValue == null || headerValue.isEmpty()) throw new GeneralException(NULL_TOKEN);
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        throw new InvalidJwtException(INVALID_OAUTH_TOKEN);
    }
}
