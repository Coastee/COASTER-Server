package com.coastee.server.login.infrastructure;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;

import static com.coastee.server.global.Constant.TOKEN_PREFIX;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_VALIDATE_TOKEN;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NULL_TOKEN;

public class JwtHeaderUtil {

    public static String getToken(
            final String tokenHeader,
            final StompHeaderAccessor accessor
    ) {
        String headerValue = accessor.getFirstNativeHeader(tokenHeader);
        return extractToken(headerValue);
    }

    public static String getToken(
            final String tokenHeader,
            final HttpServletRequest request
    ) {
        String headerValue = request.getHeader(tokenHeader);
        return extractToken(headerValue);
    }

    public static String extractToken(final String headerValue) {
        if (headerValue == null || headerValue.isEmpty()) throw new GeneralException(NULL_TOKEN);
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        throw new InvalidJwtException(FAIL_VALIDATE_TOKEN);
    }
}
