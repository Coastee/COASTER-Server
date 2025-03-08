package com.coastee.server.chatroom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ChatRoomType {
    ENTIRE("ENTIRE", "entire"),
    GROUP("GROUP", "groups"),
    MEETING("MEETING", "meetings"),
    DM("DM", "dms"),
    ;

    private final String code;
    private final String pathParam;

    public static ChatRoomType code(final String code) {
        return Arrays.stream(ChatRoomType.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElseGet(() -> null);
    }

    public static ChatRoomType url(final String url) {
        return Arrays.stream(ChatRoomType.values())
                .filter(r -> r.getPathParam().equals(url.toLowerCase()))
                .findAny()
                .orElseGet(() -> null);
    }
}
