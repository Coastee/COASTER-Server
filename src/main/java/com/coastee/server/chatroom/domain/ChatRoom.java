package com.coastee.server.chatroom.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom {
    private Long roomId;
    private String name;
    private ChatRoomType chatRoomType;

    @Builder
    public ChatRoom(
            final Long id,
            final String title,
            final ChatRoomType chatRoomType
    ) {
        this.roomId = id;
        this.name = title;
        this.chatRoomType = chatRoomType;
    }
}
