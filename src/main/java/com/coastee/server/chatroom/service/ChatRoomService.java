package com.coastee.server.chatroom.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_CHATROOM_ID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom save(final ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findById(final Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new GeneralException(INVALID_CHATROOM_ID));
    }

    public Page<ChatRoom> findAllByServerAndType(
            final Server server,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    ) {
        return chatRoomRepository.findByServerAndChatRoomType(server, chatRoomType, pageable);
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
}
