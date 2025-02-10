package com.coastee.server.dm.domain;

import com.coastee.server.chat.domain.ChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DMType {
    ENTER("ENTER"),
    QUIT("QUIT"),
    TALK("TALK"),
    DELETE("DELETE"),
    ;

    private final String code;

    public static ChatType of(final String code) {
        return Arrays.stream(ChatType.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
