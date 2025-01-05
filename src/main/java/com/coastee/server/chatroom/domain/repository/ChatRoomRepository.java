package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChatRoomRepository {
    private final Map<Long, ChatRoom> chatRooms = new HashMap<>();

    public List<ChatRoom> findAll() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findById(Long id) {
        return chatRooms.get(id);
    }

    public ChatRoom create(String title) {
        long id = (long) (Math.random() * 100);
        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .title(title)
                .chatRoomType(ChatRoomType.ENTIRE)
                .build();
        chatRooms.put(id, chatRoom);
        return chatRoom;
    }
}
