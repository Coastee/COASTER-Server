package com.coastee.server.chatroom.domain;

import com.coastee.server.community.domain.Community;
import com.coastee.server.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    private String title;

    private ChatRoomType chatRoomType;

    @Builder
    public ChatRoom(
            final Community community,
            final String title,
            final ChatRoomType chatRoomType
    ) {
        this.community = community;
        this.title = title;
        this.chatRoomType = chatRoomType;
    }
}