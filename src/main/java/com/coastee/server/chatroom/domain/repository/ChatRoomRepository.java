package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByServerAndChatRoomType(
            final Server server,
            final ChatRoomType type,
            final Pageable pageable
    );

    Page<ChatRoom> findByServerAndUserAndChatRoomType(
            final Server server,
            final User user,
            final ChatRoomType chatRoomType,
            final Pageable pageable
    );
}
