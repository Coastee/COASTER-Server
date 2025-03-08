package com.coastee.server.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Authority {
    USER("USER"),
    ADMIN("ADMIN");

    private final String code;

    @Override
    public String toString() {
        return this.code;
    }

    public static Authority of(final String code) {
        return Arrays.stream(Authority.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElseGet(() -> null);
    }
}
