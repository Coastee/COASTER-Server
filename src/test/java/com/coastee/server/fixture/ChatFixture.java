package com.coastee.server.fixture;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.ChatType;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.user.domain.User;

public class ChatFixture {

    public static Chat get(final User user, final ChatRoom chatRoom) {
        return new Chat(
                user,
                chatRoom,
                "안녕하세요",
                ChatType.TALK
        );
    }
}
