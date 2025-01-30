package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryCustomRepository;
import com.coastee.server.chatroom.domain.repository.dto.FindHasEntered;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomEntryService {
    private final ChatRoomEntryCustomRepository chatRoomEntryCustomRepository;

    @Transactional
    public ChatRoomEntry enter(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = new ChatRoomEntry(user, chatRoom);
        chatRoom.enter();
        return chatRoomEntryCustomRepository.save(chatRoomEntry);
    }

    public Map<Long, Boolean> findHasEnteredByChatRoomList(
            final User user,
            final List<ChatRoom> chatRoomList
    ) {
        return chatRoomEntryCustomRepository.findHasEnteredByChatRoomList(user, chatRoomList)
                .stream()
                .collect(Collectors.toMap(FindHasEntered::getChatRoomId, FindHasEntered::getHasJoined));
    }
}