package com.coastee.server.fixture;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;

import java.time.LocalDateTime;

public class ChatRoomFixture {
    private static final String LOCATION = "서울특별시 용산구 청파로47길 100";
    private static final String DETAIL = "숙명여자대학교 1캠퍼스 정문 앞";

    public static ChatRoom getMeeting(final Server server, final User user) {
        return ChatRoom.meetingChatRoom(
                server,
                user,
                "titleA",
                "content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LOCATION,
                DETAIL,
                5
        );
    }

    public static ChatRoom getGroup(final Server server, final User user) {
        return ChatRoom.groupChatRoom(
                server,
                user,
                "titleA",
                "content"
        );
    }
}
