package com.coastee.server.chat.controller;

import com.coastee.server.chat.domain.ChatMessage;
import com.coastee.server.chat.domain.ChatMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations operations;

    @MessageMapping("/chat/message")
    public void message(final ChatMessage message) {
        if (message.getType().equals(ChatMessageType.ENTER))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        operations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
