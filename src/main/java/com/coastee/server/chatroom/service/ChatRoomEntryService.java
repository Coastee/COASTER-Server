package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomEntryService {
    private final ChatRoomEntryRepository chatRoomEntryRepository;

    @Transactional
    public ChatRoomEntry save(final ChatRoomEntry chatRoomEntry) {
        return chatRoomEntryRepository.save(chatRoomEntry);
    }
}
