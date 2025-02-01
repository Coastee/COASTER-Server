package com.coastee.server.chat.service;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.reposistory.ChatRepository;
import com.coastee.server.chatroom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Page<Chat> findAllByChatRoom(final ChatRoom chatRoom, final Pageable pageable) {
        return chatRepository.findAllByChatRoom(chatRoom, pageable);
    }
}
