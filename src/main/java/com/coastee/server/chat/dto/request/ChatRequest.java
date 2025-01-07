package com.coastee.server.chat.dto.request;

import com.coastee.server.chat.domain.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRequest {
    private Long roomId;
    private String message;
    private ChatMessageType type;

    // test
    private String sender;
}