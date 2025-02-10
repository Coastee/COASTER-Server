package com.coastee.server.auth.infrastructurre;

import com.coastee.server.global.apipayload.code.ErrorReasonDTO;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.login.infrastructure.JwtHeaderUtil;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.coastee.server.global.domain.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final AuthProvider authProvider;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        try {
            if (servletPath.equals("/api/v1/refresh")) {
                String refreshToken = JwtHeaderUtil.getToken(HEADER_REFRESH_TOKEN, request);
                jwtProvider.validateRefreshToken(refreshToken);
                filterChain.doFilter(request, response);
            } else {
                String headerValue = request.getHeader(HEADER_AUTHORIZATION);
                if (headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) {
                    String accessToken = headerValue.substring(TOKEN_PREFIX.length());
                    if (jwtProvider.validateAccessToken(accessToken)) {
                        Authentication authentication = authProvider.getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (GeneralException e) {
            exceptionHandler(response, e);
        }
    }

    private void exceptionHandler(
            final HttpServletResponse response,
            final GeneralException error
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        ErrorReasonDTO errorReasonDTO = error.getErrorReasonHttpStatus();

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(errorReasonDTO);

        response.setStatus(errorReasonDTO.getHttpStatus().value());
        response.getWriter().write(result);
    }
}
