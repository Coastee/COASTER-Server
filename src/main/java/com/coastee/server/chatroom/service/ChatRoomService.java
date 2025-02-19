package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.domain.repository.ChatRoomQuerydslRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_FIND_SERVER_CHATROOM;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_CHATROOM_ID;
import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomQuerydslRepository chatRoomQueryDSLRepository;

    @Transactional
    public ChatRoom save(final ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findById(final Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new GeneralException(INVALID_CHATROOM_ID));
    }

    public ChatRoom findEntireChatRoomByServer(final Server server) {
        List<ChatRoom> chatRoomList = findAllByServerAndType(server, ChatRoomType.ENTIRE, PageRequest.of(0, 1))
                .getContent();
        if (chatRoomList.isEmpty()) throw new GeneralException(FAIL_FIND_SERVER_CHATROOM);
        return chatRoomList.get(0);
    }

    public List<ChatRoom> findEntireChatRoomsByServers(final List<Server> serverList) {
        List<ChatRoom> chatRoomList = findAllByServersAndType(
                serverList,
                ChatRoomType.ENTIRE,
                PageRequest.of(0, DEFAULT_PAGING_SIZE)
        ).getContent();
        if (chatRoomList.size() != serverList.size()) throw new GeneralException(FAIL_FIND_SERVER_CHATROOM);
        return chatRoomList;
    }

    public Page<ChatRoom> findAllByServerAndType(
            final Server server,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    ) {
        return chatRoomRepository.findByServerAndChatRoomType(server, chatRoomType, pageable);
    }

    public Page<ChatRoom> findAllByServersAndType(
            final List<Server> serverList,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    ) {
        return chatRoomRepository.findByServerInAndChatRoomType(serverList, chatRoomType, pageable);
    }

    public Page<ChatRoom> findAllByServerAndParticipantAndType(
            final Server server,
            final User user,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    ) {
        return chatRoomRepository.findByServerAndParticipantAndChatRoomType(server, user, chatRoomType, pageable);
    }

    public Page<ChatRoom> findAllByServerAndUserAndType(
            final Server server,
            final User user,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    ) {
        return chatRoomRepository.findByServerAndUserAndChatRoomType(server, user, chatRoomType, pageable);
    }

    public Page<ChatRoom> findByServerAndTypeAndKeywordAndTagList(
            final Server server,
            final ChatRoomType type,
            final String keyword,
            final List<HashTag> tagList,
            final Pageable pageable
    ) {
        return chatRoomQueryDSLRepository.findByServerAndTypeAndKeywordAndTagList(
                server,
                type,
                keyword,
                tagList,
                pageable
        );
    }
}
