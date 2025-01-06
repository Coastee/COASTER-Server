package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.repository.ChatRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    public List<ChatRoom> findAll() {
        return chatRoomRedisRepository.findAll();
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRedisRepository.findById(roomId);
    }

    public ChatRoom createRoom(String title) {
        return chatRoomRedisRepository.create(title);
    }
}
