package com.coastee.server.image.domain;

import com.coastee.server.auth.domain.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DirName {
    USER("user"),
    CHAT("chat"),
    CHATROOM("chatroom"),
    ;

    private final String code;

    @Override
    public String toString() {
        return this.code;
    }

    public static Authority of(final String code) {
        return Arrays.stream(Authority.values())
                .filter(r -> r.getCode().equals(code.toLowerCase()))
                .findAny()
                .orElse(null);
    }
}
