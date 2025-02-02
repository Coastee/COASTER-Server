package com.coastee.server.fixture;

import com.coastee.server.user.domain.SocialType;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.UserIntro;

import java.time.LocalDateTime;
import java.util.List;

public class UserFixture {
    private static final String PROFILE_IMAGE = "https://avatars.githubusercontent.com/u/193146868?s=200&v=4";
    private static final List<String> URL_LIST = List.of(
            "https://github.com/Coastee",
            "https://github.com/Coastee/COASTER-Client",
            "https://github.com/Coastee/COASTER-Server"
    );
    private static final String REFRESH_TOKEN = "refresh-token";

    public static User get() {
        return new User(
                "user",
                LocalDateTime.now(),
                "user@naver.com",
                new UserIntro("headline", "developer", 5),
                "bio",
                PROFILE_IMAGE,
                REFRESH_TOKEN,
                URL_LIST,
                SocialType.NAVER,
                "SOCIAL_ID"
        );
    }

    public static User get(final String nickname) {
        return new User(
                nickname,
                LocalDateTime.now(),
                "user@naver.com",
                new UserIntro("headline", "developer", 5),
                "bio",
                PROFILE_IMAGE,
                REFRESH_TOKEN,
                URL_LIST,
                SocialType.NAVER,
                "SOCIAL_ID"
        );
    }
}
