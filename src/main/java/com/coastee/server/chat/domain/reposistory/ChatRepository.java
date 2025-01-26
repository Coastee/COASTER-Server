package com.coastee.server.chat.domain.reposistory;

import com.coastee.server.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
