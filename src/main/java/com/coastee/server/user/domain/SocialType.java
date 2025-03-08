package com.coastee.server.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    LINKEDIN("LINKEDIN");

    private final String code;

    public static SocialType of(final String code) {
        return Arrays.stream(SocialType.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElseGet(() -> null);
    }
}
