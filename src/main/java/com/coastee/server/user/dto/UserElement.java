package com.coastee.server.user.dto;

import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class UserElement {
    private Long id;
    private String profileImage;
    private String nickname;
    private Boolean linkedInVerify;
    private UserIntroElement userIntro;

    public UserElement(final User user) {
        this.id = user.getId();
        this.profileImage = user.getProfileImage();
        this.nickname = user.getNickname();
        this.linkedInVerify = user.getLinkedInVerify();
        this.userIntro = new UserIntroElement(user.getUserIntro());
    }
}
