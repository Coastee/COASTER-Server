package com.coastee.server.global.redis;

import com.coastee.server.chat.domain.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations operations;

    @Override
    public void onMessage(
            final Message message,
            final byte[] pattern
    ) {
        try {
            String publishMessage = (String) redisTemplate
                    .getStringSerializer()
                    .deserialize(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            operations.convertAndSend(
                    "/sub/chat/room/" + chatMessage.getChatRoom().getId(),
                    chatMessage
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
