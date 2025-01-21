package com.coastee.server.image.domain;

import com.coastee.server.auth.domain.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DirName {
    USER("user"), POST("post"), CHAT("chat");

    private final String code;

    @Override
    public String toString() {
        return this.code;
    }

    public static Authority of(final String code) {
        return Arrays.stream(Authority.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
