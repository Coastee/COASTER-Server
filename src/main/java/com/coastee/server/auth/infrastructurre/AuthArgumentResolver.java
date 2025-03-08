package com.coastee.server.auth.infrastructurre;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.auth.domain.Authority;
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

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

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
        return getAccessor();
    }

    public Accessor resolveArgument() {
        return getAccessor();
    }

    private Accessor getAccessor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            final Long userId = Long.valueOf(authentication.getPrincipal().toString());
            if (isAdmin(authentication)) return Accessor.admin(userId);
            if (isUser(authentication)) return Accessor.user(userId);
            throw new AuthenticationException(_INVALID_AUTHORITY);
        } catch (NumberFormatException | NullPointerException e) {
            throw new AuthenticationException(_INVALID_AUTHORITY);
        }
    }

    private boolean isAdmin(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authority.ADMIN.toString()));
    }

    private boolean isUser(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Authority.USER.toString()));
    }
}