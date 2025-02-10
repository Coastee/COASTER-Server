package com.coastee.server.user.domain;

import com.coastee.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private LocalDateTime birthDate;

    private String email;

    @Embedded
    private UserIntro userIntro = new UserIntro();

    private String bio;

    private String profileImage;

    private String refreshToken;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> urlList = new ArrayList<>();

    @Enumerated(STRING)
    private SocialType socialType;

    private String socialId;

    private String linkedInId;

    private Boolean linkedInVerify;

    public User(
            final String nickname,
            final LocalDateTime birthDate,
            final String email,
            final UserIntro userIntro,
            final String bio,
            final String profileImage,
            final String refreshToken,
            final List<String> urlList,
            final SocialType socialType,
            final String socialId
    ) {
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.email = email;
        this.userIntro = userIntro;
        this.bio = bio;
        this.profileImage = profileImage;
        this.refreshToken = refreshToken;
        this.urlList = urlList;
        this.socialType = socialType;
        this.socialId = socialId;
        this.linkedInVerify = false;
    }

    public User(
            final String nickname,
            final String email,
            final SocialType socialType,
            final String socialId
    ) {
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
        this.socialId = socialId;
        this.linkedInVerify = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User that = (User) o;
        return id != null && Objects.equals(id, that.id);
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateUrlList(final List<String> urlList) {
        this.urlList = urlList;
    }

    public void updateUserIntro(
            final String headline,
            final String job,
            final int expYears
    ) {
        this.userIntro = new UserIntro(headline, job, expYears);
    }

    public void updateBio(final String bio) {
        this.bio = bio;
    }

    public void updateProfileImage(final String profileImage) {
        this.profileImage = profileImage;
    }

    public void verify(final String linkedInId) {
        this.linkedInId = linkedInId;
        this.linkedInVerify = true;
    }
}
