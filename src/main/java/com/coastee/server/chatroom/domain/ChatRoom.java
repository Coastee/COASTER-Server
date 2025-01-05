package com.coastee.server.chatroom.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom {
    private Long id;
    private String title;
    private ChatRoomType chatRoomType;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(
            final Long id,
            final String title,
            final ChatRoomType chatRoomType
    ) {
        this.id = id;
        this.title = title;
        this.chatRoomType = chatRoomType;
    }
}
