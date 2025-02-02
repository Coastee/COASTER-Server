package com.coastee.server.fixture;

import com.coastee.server.hashtag.domain.HashTag;

import java.util.List;

public class HashTagFixture {

    public static HashTag get() {
        return new HashTag("#coaster");
    }

    public static List<HashTag> getAll() {
        return List.of(
                new HashTag("#coaster"),
                new HashTag("#server"),
                new HashTag("#client"),
                new HashTag("#숙명여대"),
                new HashTag("#sookmyung"),
                new HashTag("#cs"),
                new HashTag("#capstone"),
                new HashTag("#졸업작품"),
                new HashTag("#코스티"),
                new HashTag("#패스기원")
        );
    }

    public static HashTag get(final String content) {
        return new HashTag(content);
    }
}
