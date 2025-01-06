package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
