package com.coastee.server.chatroom.domain;

import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
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
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String thumbnail;

    private String title;

    private String content;

    private ChatRoomType chatRoomType;

    @Builder(builderMethodName = "of")
    public ChatRoom(
            final Server server,
            final User user,
            final String thumbnail,
            final String title,
            final String content,
            final ChatRoomType chatRoomType
    ) {
        this.server = server;
        this.user = user;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.chatRoomType = chatRoomType;
    }
}