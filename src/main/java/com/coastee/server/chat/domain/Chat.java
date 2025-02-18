package com.coastee.server.chat.domain;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
public class Chat extends BaseEntity {
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

    @Enumerated(EnumType.STRING)
    private ChatType type;

    public Chat(
            final User user,
            final ChatRoom chatRoom,
            final String content,
            final ChatType chatType
    ) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.content = content;
        this.type = chatType;
    }
}
