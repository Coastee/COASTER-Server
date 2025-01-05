package com.coastee.server.global.handler;

import com.coastee.server.chat.domain.ChatMessage;
import com.coastee.server.chat.domain.ChatMessageType;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatRoomService chatRoomService;

    @Override
    protected void handleTextMessage(
            final WebSocketSession session,
            final TextMessage message
    ) throws Exception {
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatRoomService.findById(chatMessage.getRoomId());
        handleActions(room, session, chatMessage);
    }

    private void handleActions(
            final ChatRoom chatRoom,
            final WebSocketSession session,
            final ChatMessage chatMessage
    ) {
        Set<WebSocketSession> sessions = chatRoom.getSessions();
        if (chatMessage.getMessageType().equals(ChatMessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setContent(chatMessage.getSender() + "님이 입장했습니다.");
        } else if (chatMessage.getMessageType().equals(ChatMessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setContent(chatMessage.getSender() + "님이 퇴장했습니다..");
        }
        sendToSocket(sessions, chatMessage);
    }

    private void sendToSocket(
            final Set<WebSocketSession> sessions,
            final ChatMessage message
    ) {
        sessions.parallelStream().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
