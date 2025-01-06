package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {
    private final Map<Long, ChatRoom> chatRooms = new HashMap<>();

    public List<ChatRoom> findAll() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>(this.chatRooms.values());
        Collections.reverse(chatRooms);
        return chatRooms;
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
