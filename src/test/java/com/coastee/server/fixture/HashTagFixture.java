package com.coastee.server.fixture;

import com.coastee.server.hashtag.domain.HashTag;

public class HashTagFixture {

    public static HashTag get() {
        return new HashTag("#coaster");
    }

    public static HashTag get(final String content) {
        return new HashTag(content);
    }
}
