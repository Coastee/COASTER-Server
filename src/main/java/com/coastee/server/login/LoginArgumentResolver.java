package com.coastee.server.login;


import com.coastee.server.login.auth.Auth;
import com.coastee.server.login.auth.domain.Accessor;
import com.coastee.server.login.auth.domain.Authority;
import com.coastee.server.global.apipayload.exception.handler.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus._INVALID_AUTHORITY;

@RequiredArgsConstructor
@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter
                .withContainingClass(Long.class)
                .hasParameterAnnotation(Auth.class);
    }

    @Override
    public Accessor resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            final Long memberId = Long.valueOf(authentication.getPrincipal().toString());
            if (isAdmin(authentication)) return Accessor.admin(memberId);
            if (isMember(authentication)) return Accessor.user(memberId);
            throw new AuthenticationException(_INVALID_AUTHORITY);
        } catch (NumberFormatException | NullPointerException e) {
            throw new AuthenticationException(_INVALID_AUTHORITY);
        }
    }

    private boolean isAdmin(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authority.ADMIN.toString()));
    }

    private boolean isMember(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authority.USER.toString()));
    }
}