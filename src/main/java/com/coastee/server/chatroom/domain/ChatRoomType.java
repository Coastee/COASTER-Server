package com.coastee.server.chatroom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ChatRoomType {
    ENTIRE("ENTIRE"),
    GROUP("GROUP"),
    MEETING("MEETING");

    private final String code;

    public static ChatRoomType of(final String code) {
        return Arrays.stream(ChatRoomType.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
