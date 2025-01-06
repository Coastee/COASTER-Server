package com.coastee.server.user.domain;

import com.coastee.server.global.BaseEntity;
import jakarta.persistence.*;
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

    private String email;

    private String profileImage;

    private String refreshToken;

    @Enumerated(STRING)
    private SocialType socialType;

    private String socialId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Experience> experienceList = new ArrayList<>();
}
