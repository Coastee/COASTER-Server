package com.coastee.server.chat.dto.request;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.ChatType;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRequest {
    private String content;

    public Chat toEntity(
            final User user,
            final ChatRoom chatRoom,
            final ChatType type
    ) {
        return new Chat(user, chatRoom, content, type);
    }
}