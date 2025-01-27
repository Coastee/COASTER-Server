package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryRepository;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomEntryService {
    private final ChatRoomEntryRepository chatRoomEntryRepository;

    @Transactional
    public ChatRoomEntry enter(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = new ChatRoomEntry(user, chatRoom);
        chatRoom.enter();
        return chatRoomEntryRepository.save(chatRoomEntry);
    }
}
