package com.coastee.server.auth;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.exception.handler.AuthenticationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus._INVALID_AUTHORITY;

@Aspect
@Component
public class UserOnlyChecker {
    @Before("@annotation(com.coastee.server.auth.UserOnly)")
    public void check(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(Accessor.class::isInstance)
                .map(Accessor.class::cast)
                .filter(a -> a.isUser() || a.isAdmin())
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(_INVALID_AUTHORITY));
    }
}
