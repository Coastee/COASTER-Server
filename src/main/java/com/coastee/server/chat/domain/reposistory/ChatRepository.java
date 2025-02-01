package com.coastee.server.chat.domain.reposistory;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chatroom.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Chat> findAllByChatRoom(final ChatRoom chatRoom, final Pageable pageable);
}
