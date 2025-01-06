package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoomEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {
}
