package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.global.domain.BaseEntityStatus;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {

    Optional<ChatRoomEntry> findByUserAndChatRoom(
            final User user,
            final ChatRoom chatRoom
    );

    @EntityGraph(attributePaths = {"user"})
    Page<ChatRoomEntry> findAllByChatRoomAndStatus(
            final ChatRoom chatRoom,
            final BaseEntityStatus status,
            final Pageable pageable
    );

    @Modifying
    @Query(value = """
            update ChatRoomEntry e set e.status = "DELETED"
            where e.user = :user
            and e.chatRoom in (select c from ChatRoom c where c.server = :server)
            """)
    void deleteAllByUserAndServer(
            @Param("user") final User user,
            @Param("server") final Server server
    );

    @Modifying
    @Query("""
            update ChatRoomEntry e set e.status = "DELETED"
            where e.chatRoom = :chatRoom
            """)
    void deleteAllByChatRoom(@Param("chatRoom") final ChatRoom chatRoom);

    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatRoomEntry> findByUserAndChatRoomIn(final User user, final List<ChatRoom> chatRoomList);
}
