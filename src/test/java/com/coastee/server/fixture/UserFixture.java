package com.coastee.server.fixture;

import com.coastee.server.user.domain.SocialType;
import com.coastee.server.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserFixture {

    public static User get() {
        return User.of()
                .nickname("user")
                .birthDate(LocalDateTime.now())
                .email("user@naver.com")
                .headline("headline")
                .bio("bio")
                .profileImage("https://avatars.githubusercontent.com/u/193146868?s=200&v=4")
                .urlList(
                        List.of(
                                "https://github.com/Coastee",
                                "https://github.com/Coastee/COASTER-Client",
                                "https://github.com/Coastee/COASTER-Server"
                        )
                )
                .socialType(SocialType.NAVER)
                .socialId("SOCIAL-ID")
                .build();
    }
}
