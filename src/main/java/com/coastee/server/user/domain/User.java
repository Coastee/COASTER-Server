package com.coastee.server.user.domain;

import com.coastee.server.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

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

    private String name;

    private String headline;

    private String bio;

    private String profileImage;

    private String refreshToken;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> urlList = new ArrayList<>();

    @Enumerated(STRING)
    private SocialType socialType;

    private String socialId;

    @Builder(builderMethodName = "of")
    public User(
            final String name,
            final String headline,
            final String bio,
            final String profileImage,
            final String refreshToken,
            final List<String> urlList,
            final SocialType socialType,
            final String socialId
    ) {
        this.name = name;
        this.headline = headline;
        this.bio = bio;
        this.profileImage = profileImage;
        this.refreshToken = refreshToken;
        this.urlList = urlList;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
