package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoomTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomTagRepository extends JpaRepository<ChatRoomTag, Long> {
}
