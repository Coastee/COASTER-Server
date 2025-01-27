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

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            select c from ChatRoom c
            right join ChatRoomEntry e
                on c = e.chatRoom
                and c.server = :server
                and :user = e.user
                and e.status = 'ACTIVE'
            """)
    Page<ChatRoom> findByServerAndParticipantAndChatRoomType(
            @Param("server") final Server server,
            @Param("user") final User user,
            @Param("type") final ChatRoomType chatRoomType,
            final Pageable pageable
    );
}
