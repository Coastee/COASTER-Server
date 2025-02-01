package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {

    Optional<ChatRoomEntry> findByUserAndChatRoom(
            final User user,
            final ChatRoom chatRoom
    );
}
