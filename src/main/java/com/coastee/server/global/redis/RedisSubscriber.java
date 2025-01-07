package com.coastee.server.global.redis;

import com.coastee.server.chat.dto.request.ChatRequest;
import com.coastee.server.global.apipayload.exception.handler.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.JSON_EXCEPTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations operations;

    public void sendMessage(final String publishMessage) {
        try {
            log.info("==sub== " + publishMessage);
            ChatRequest chatRequest = objectMapper.readValue(
                    publishMessage,
                    ChatRequest.class
            );
            operations.convertAndSend(
                    "/sub/chat/room/" + chatRequest.getRoomId(),
                    chatRequest
            );
        } catch (JsonProcessingException e) {
            throw new JsonException(JSON_EXCEPTION);
        }
    }
}
