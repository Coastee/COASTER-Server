package com.coastee.server.dm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DMType {
    CREATE("CREATE"),
    QUIT("QUIT"),
    TALK("TALK"),
    DELETE("DELETE"),
    ;

    private final String code;

    public static DMType of(final String code) {
        return Arrays.stream(DMType.values())
                .filter(r -> r.getCode().equals(code.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
