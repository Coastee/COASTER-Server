package com.coastee.server.fixture;

import com.coastee.server.user.domain.SocialType;
import com.coastee.server.user.domain.User;

public class UserFixture {

    public static User get() {
        return User.of()
                .name("user")
                .email("aaa2naver.com")
                .socialType(SocialType.NAVER)
                .socialId("ABCDE")
                .build();
    }
}
