package com.coastee.server.user.dto.response;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ProfileResponse {
    private Long userId;
    private String name;
    private String headline;
    private String bio;
    private String profileImage;
    private List<String> urlList;
    private List<Experience> experienceList;

    public static ProfileResponse from(final User user) {
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getHeadline(),
                user.getBio(),
                user.getProfileImage(),
                user.getUrlList(),
                user.getExperienceList()
        );
    }
}
