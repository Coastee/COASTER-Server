package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryCustomRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryRepository;
import com.coastee.server.chatroom.domain.repository.dto.FindHasEntered;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NOT_IN_CHATROOM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomEntryService {
    private final ChatRoomEntryRepository chatRoomEntryRepository;
    private final ChatRoomEntryCustomRepository chatRoomEntryCustomRepository;

    @Transactional
    public ChatRoomEntry enter(final User user, final ChatRoom chatRoom) {
        chatRoom.enter();
        ChatRoomEntry chatRoomEntry = chatRoomEntryRepository
                .findByUserAndChatRoom(user, chatRoom)
                .orElse(createAndSave(user, chatRoom));
        chatRoomEntry.activate();
        return chatRoomEntry;
    }

    private ChatRoomEntry createAndSave(final User user, final ChatRoom chatRoom) {
        return chatRoomEntryRepository.save(new ChatRoomEntry(user, chatRoom));
    }

    @Transactional
    public void exit(final User user, final ChatRoom chatRoom) {
        chatRoom.exit();
        ChatRoomEntry chatRoomEntry = validateJoin(user, chatRoom);
        chatRoomEntry.delete();
    }

    public ChatRoomEntry validateJoin(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = chatRoomEntryRepository
                .findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new GeneralException(NOT_IN_CHATROOM));
        if (chatRoomEntry.isDeleted()) throw new GeneralException(NOT_IN_CHATROOM);
        return chatRoomEntry;
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