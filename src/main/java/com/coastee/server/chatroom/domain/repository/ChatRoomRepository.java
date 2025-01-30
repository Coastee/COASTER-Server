package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<ChatRoom> findByServerAndChatRoomType(
            final Server server,
            final ChatRoomType type,
            final Pageable pageable
    );

    Page<ChatRoom> findByServerAndUserAndChatRoomType(
            final Server server,
            final User user,
            final ChatRoomType type,
            final Pageable pageable
    );

    @Query("""
            select c from ChatRoom c
            join ChatRoomEntry e
                on c = e.chatRoom
                and e.user = :user
                and e.status = 'ACTIVE'
                and c.server = :server
                and c.user != :user
            """)
    Page<ChatRoom> findByServerAndParticipantAndChatRoomType(
            @Param("server") final Server server,
            @Param("user") final User user,
            @Param("type") final ChatRoomType chatRoomType,
            final Pageable pageable
    );
}
