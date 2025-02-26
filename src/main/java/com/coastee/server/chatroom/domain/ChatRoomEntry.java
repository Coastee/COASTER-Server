package com.coastee.server.chatroom.domain;

import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomEntry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private boolean favorite;

    public ChatRoomEntry(final User user, final ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.favorite = false;
    }

    public void toggleFavorite() {
        this.favorite = !this.favorite;
    }
}
