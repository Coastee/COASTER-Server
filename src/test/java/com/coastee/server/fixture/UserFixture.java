package com.coastee.server.fixture;

import com.coastee.server.user.domain.SocialType;
import com.coastee.server.user.domain.User;

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
                .socialType(SocialType.GOOGLE)
                .socialId("ABCDE")
                .build();
    }
}
