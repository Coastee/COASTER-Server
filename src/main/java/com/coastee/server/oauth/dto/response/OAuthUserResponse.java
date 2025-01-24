package com.coastee.server.oauth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class OAuthUserResponse {
    private Long userId;
    private String socialId;
    private String name;
    private String email;
    private String handle;
    private String socialType;
    private String accessToken;
    private String refreshToken;
    private Boolean isNewMember;
}
