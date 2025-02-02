package com.coastee.server.fixture;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.ChatType;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.user.domain.User;

import java.util.List;

public class ChatFixture {

    public static Chat get(final User user, final ChatRoom chatRoom) {
        return new Chat(user, chatRoom, "안녕하세요", ChatType.TALK);
    }

    public static List<Chat> getAll(final User user, final ChatRoom chatRoom) {
        return List.of(
                new Chat(user, chatRoom, "안녕하세요", ChatType.TALK),
                new Chat(user, chatRoom, "감사해요", ChatType.TALK),
                new Chat(user, chatRoom, "잘있어요", ChatType.TALK),
                new Chat(user, chatRoom, "다시만나요", ChatType.TALK),
                new Chat(user, chatRoom, "아침해가 뜨면", ChatType.TALK),
                new Chat(user, chatRoom, "매일같은 사람들과", ChatType.TALK),
                new Chat(user, chatRoom, "또 다시 새로운", ChatType.TALK),
                new Chat(user, chatRoom, "하루일을 시작해", ChatType.TALK)
        );
    }
}
