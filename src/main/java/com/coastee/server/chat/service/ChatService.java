package com.coastee.server.chat.service;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.reposistory.ChatRepository;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.global.apipayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_CHAT_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Chat findById(final Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new GeneralException(INVALID_CHAT_ID));
    }

    public Page<Chat> findAllByChatRoom(final ChatRoom chatRoom, final Pageable pageable) {
        return chatRepository.findAllByChatRoom(chatRoom, pageable);
    }

    @Transactional
    public Chat save(final Chat chat) {
        return chatRepository.save(chat);
    }
}
