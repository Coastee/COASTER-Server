package com.coastee.server.chat.domain;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.global.BaseEntity;
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
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private String content;

    private ChatMessageType type;

    @Builder
    public ChatMessage(
            final User user,
            final ChatRoom chatRoom,
            final String content,
            final ChatMessageType chatMessageType
    ) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.content = content;
        this.type = chatMessageType;
    }
}
