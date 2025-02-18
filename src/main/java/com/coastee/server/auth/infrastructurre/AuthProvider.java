package com.coastee.server.auth.infrastructurre;

import com.coastee.server.login.infrastructure.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.coastee.server.global.domain.Constant.AUTHORITIES_KEY;

@Component
@RequiredArgsConstructor
public class AuthProvider {
    private final JwtProvider jwtProvider;

    public Authentication getAuthentication(final String accessToken) {
        Claims claims = jwtProvider.getTokenClaims(accessToken);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                jwtProvider.getSubject(accessToken),
                accessToken,
                authorities
        );
    }
}
