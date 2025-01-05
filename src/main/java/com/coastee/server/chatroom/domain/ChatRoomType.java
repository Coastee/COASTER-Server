package com.coastee.server.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatRoomType {
    ENTIRE("entire"),
    GROUP("group"),
    MEETING("meeting");

    private final String code;

    public static ChatRoomType of(final String code) {
        return Arrays.stream(ChatRoomType.values())
                .filter(r -> r.getCode().equals(code.toLowerCase()))
                .findAny()
                .orElse(null);
    }
}
