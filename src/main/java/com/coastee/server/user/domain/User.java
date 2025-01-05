package com.coastee.server.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {
    private Long id;
    private String name;
    private String headline;
    private String bio;
    private String email;
    private String profileImage;
    private String refreshToken;
    private SocialType socialType;
    private String socialId;
}
