package com.coastee.server.global.stomp.infrastructure;

import com.coastee.server.chat.dto.ChatElement;
import com.coastee.server.dmroom.dto.redis.DMSendRequest;
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

    public void sendChat(final String publishMessage) {
        try {
            ChatElement chatElement = objectMapper.readValue(
                    publishMessage,
                    ChatElement.class
            );
            operations.convertAndSend(
                    "/sub/chats/" + chatElement.getChatRoomId(),
                    chatElement
            );
        } catch (JsonProcessingException e) {
            throw new JsonException(JSON_EXCEPTION);
        }
    }

    public void sendDM(final String publishMessage) {
        try {
            DMSendRequest sendRequest = objectMapper.readValue(
                    publishMessage,
                    DMSendRequest.class
            );

            operations.convertAndSend(
                    "/sub/dms/" + sendRequest.getDmRoom().getId(),
                    sendRequest.getDm()
            );
            operations.convertAndSend(
                    "/sub/users/dms/" + sendRequest.getReceiverId(),
                    sendRequest.getDmRoom()
            );
            operations.convertAndSend(
                    "/sub/users/dms/" + sendRequest.getSenderId(),
                    sendRequest.getDmRoom()
            );
        } catch (JsonProcessingException e) {
            throw new JsonException(JSON_EXCEPTION);
        }
    }
}
