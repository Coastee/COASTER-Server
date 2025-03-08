package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryCustomRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomEntryRepository;
import com.coastee.server.chatroom.domain.repository.dto.FindHasEntered;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.domain.BaseEntityStatus;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ChatRoomEntry> findAllByChatRoom(final ChatRoom chatRoom, final Pageable pageable) {
        return chatRoomEntryRepository.findAllByChatRoomAndStatus(chatRoom, BaseEntityStatus.ACTIVE, pageable);
    }

    public Map<Long, Boolean> findHasEnteredByChatRoomList(
            final User user,
            final List<ChatRoom> chatRoomList
    ) {
        return chatRoomEntryCustomRepository.findHasEnteredByChatRoomList(user, chatRoomList)
                .stream()
                .collect(Collectors.toMap(FindHasEntered::getChatRoomId, FindHasEntered::getHasJoined));
    }

    @Transactional
    public ChatRoomEntry enter(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = chatRoomEntryRepository
                .findByUserAndChatRoom(user, chatRoom)
                .orElseGet(() -> createAndSave(user, chatRoom));
        if (chatRoomEntry.isDeleted()) {
            chatRoom.enter();
            chatRoomEntry.activate();
        }
        return chatRoomEntry;
    }

    @Transactional
    public List<ChatRoomEntry> enter(final User user, final List<ChatRoom> chatRoomList) {
        List<ChatRoomEntry> entryList = chatRoomList.stream().map(
                chatRoom -> {
                    chatRoom.enter();
                    return new ChatRoomEntry(user, chatRoom);
                }
        ).toList();
        return chatRoomEntryRepository.saveAll(entryList);
    }

    private ChatRoomEntry createAndSave(final User user, final ChatRoom chatRoom) {
        chatRoom.enter();
        return chatRoomEntryRepository.save(new ChatRoomEntry(user, chatRoom));
    }

    @Transactional
    public void toggleFavorite(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = validateJoin(user, chatRoom);
        chatRoomEntry.toggleFavorite();
    }

    @Transactional
    public void exit(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = validateJoin(user, chatRoom);
        chatRoom.exit();
        chatRoomEntry.delete();
    }

    @Transactional
    public void exit(final User user, final Server server) {
        chatRoomEntryRepository.deleteAllByUserAndServer(user, server);
    }

    public ChatRoomEntry validateJoin(final User user, final ChatRoom chatRoom) {
        ChatRoomEntry chatRoomEntry = chatRoomEntryRepository
                .findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new GeneralException(NOT_IN_CHATROOM));
        if (chatRoomEntry.isDeleted()) throw new GeneralException(NOT_IN_CHATROOM);
        return chatRoomEntry;
    }
}