package com.coastee.server.chat.controller;

import com.coastee.server.auth.MemberOnly;
import com.coastee.server.chat.domain.ChatMessage;
import com.coastee.server.chat.domain.ChatMessageType;
import com.coastee.server.chatroom.domain.repository.ChatRoomRedisRepository;
import com.coastee.server.global.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRedisRepository chatRoomRedisRepository;

//    @MessageMapping("/chat/message")
//    @MemberOnly
//    public void message(final ChatMessage message) {
//        if (message.getType().equals(ChatMessageType.ENTER)) {
//            chatRoomRedisRepository.enterChatRoom(message.getChatRoom().getId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        }
//        redisPublisher.publish(chatRoomRedisRepository.getTopic(message.getChatRoom().getId()), message);
//    }
}
