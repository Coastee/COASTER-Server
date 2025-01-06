package com.coastee.server.chat.controller;

import com.coastee.server.chat.domain.ChatMessage;
import com.coastee.server.chat.domain.ChatMessageType;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.global.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message")
    public void message(final ChatMessage message) {
        if (message.getType().equals(ChatMessageType.ENTER)) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}
