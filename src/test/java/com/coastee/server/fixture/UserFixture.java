package com.coastee.server.fixture;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.SocialType;
import com.coastee.server.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserFixture {

    public static User get() {
        return User.of()
                .name("user")
                .headline("headline")
                .bio("bio")
                .profileImage("http://profileImage")
                .refreshToken("refreshToken")
                .urlList(
                        List.of(
                                "http://a.com",
                                "http://b.com"
                        ))
                .experienceList(
                        List.of(
                                Experience.of()
                                        .title("title1")
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(100))
                                        .content("first experience")
                                        .build(),
                                Experience.of()
                                        .title("title2")
                                        .startDate(LocalDateTime.now().minusDays(100))
                                        .endDate(LocalDateTime.now())
                                        .content("second experience")
                                        .build()
                        ))
                .socialType(SocialType.GOOGLE)
                .socialId("ABCDE")
                .build();
    }
}
