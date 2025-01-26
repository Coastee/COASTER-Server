package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom save(final ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }
}
